package com.github.mkopylec.charon.core.http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletResponse;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer.Context;
import com.github.mkopylec.charon.configuration.CharonProperties;
import com.github.mkopylec.charon.configuration.MappingProperties;
import com.github.mkopylec.charon.core.balancer.LoadBalancer;
import com.github.mkopylec.charon.core.mappings.MappingsProvider;
import com.github.mkopylec.charon.core.trace.ProxyingTraceInterceptor;
import com.github.mkopylec.charon.exceptions.CharonException;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.InputStreamEntity;
import org.slf4j.Logger;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.RetryContext;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import static com.codahale.metrics.MetricRegistry.name;
import static com.github.mkopylec.charon.configuration.RetryingProperties.MAPPING_NAME_RETRY_ATTRIBUTE;
import static com.github.mkopylec.charon.core.utils.UriCorrector.correctUri;
import static java.nio.charset.Charset.forName;
import static java.util.Arrays.stream;
import static org.apache.commons.io.IOUtils.toByteArray;
import static org.apache.commons.lang3.StringUtils.prependIfMissing;
import static org.apache.commons.lang3.StringUtils.removeStart;
import static org.springframework.http.ResponseEntity.status;

public class RequestForwarder {

    private static final Logger log = getLogger(RequestForwarder.class);

    protected final ServerProperties server;
    protected final CharonProperties charon;
    protected final HttpClientProvider httpClientProvider;
    protected final MappingsProvider mappingsProvider;
    protected final LoadBalancer loadBalancer;
    protected final MetricRegistry metricRegistry;
    protected final ProxyingTraceInterceptor traceInterceptor;
    protected final ForwardedRequestInterceptor forwardedRequestInterceptor;
    protected final ReceivedResponseInterceptor receivedResponseInterceptor;

    public RequestForwarder(
            ServerProperties server,
            CharonProperties charon,
            HttpClientProvider httpClientProvider,
            MappingsProvider mappingsProvider,
            LoadBalancer loadBalancer,
            MetricRegistry metricRegistry,
            ProxyingTraceInterceptor traceInterceptor,
            ForwardedRequestInterceptor forwardedRequestInterceptor,
            ReceivedResponseInterceptor receivedResponseInterceptor
    ) {
        this.server = server;
        this.charon = charon;
        this.httpClientProvider = httpClientProvider;
        this.mappingsProvider = mappingsProvider;
        this.loadBalancer = loadBalancer;
        this.metricRegistry = metricRegistry;
        this.traceInterceptor = traceInterceptor;
        this.forwardedRequestInterceptor = forwardedRequestInterceptor;
        this.receivedResponseInterceptor = receivedResponseInterceptor;
    }

    public ResponseEntity<byte[]> forwardHttpRequest(RequestData data, String traceId, RetryContext context, MappingProperties mapping) {
        forwardedRequestInterceptor.intercept(data);
        ForwardDestination destination = resolveForwardDestination(data.getUri(), mapping);
        traceInterceptor.onForwardStart(traceId, destination.getMappingName(), data.getMethod(), destination.getUri().toString(), data.getBody(), data.getHeaders());
        context.setAttribute(MAPPING_NAME_RETRY_ATTRIBUTE, destination.getMappingName());
//        RequestEntity<byte[]> request = new RequestEntity<>(data.getBody(), data.getHeaders(), data.getMethod(), destination.getUri());
        ResponseData response = sendRequest(traceId, request, mapping, destination.getMappingMetricsName());

        log.info("Forwarding: {} {} -> {} {}", data.getMethod(), data.getUri(), destination.getUri(), response.getStatus().value());

        traceInterceptor.onForwardComplete(traceId, response.getStatus(), response.getBody(), response.getHeaders());
        receivedResponseInterceptor.intercept(response);

        return status(response.getStatus())
                .headers(response.getHeaders())
                .body(response.getBody());
    }

    protected ForwardDestination resolveForwardDestination(String originUri, MappingProperties mapping) {
        return new ForwardDestination(createDestinationUrl(originUri, mapping), mapping.getName(), resolveMetricsName(mapping));
    }

    protected URI createDestinationUrl(String uri, MappingProperties mapping) {
        if (mapping.isStripPath()) {
            uri = stripMappingPath(uri, mapping);
        }
        String host = loadBalancer.chooseDestination(mapping.getDestinations());
        try {
            return new URI(host + uri);
        } catch (URISyntaxException e) {
            throw new CharonException("Error creating destination URL from HTTP request URI: " + uri + " using mapping " + mapping, e);
        }
    }

    protected ResponseData sendRequest(String traceId, RequestData data, MappingProperties mapping, ForwardDestination destination, final HttpServletResponse response) throws IOException {
        Context context = null;
        if (charon.getMetrics().isEnabled()) {
            context = metricRegistry.timer(destination.getMappingMetricsName()).time();
        }
        HttpUriRequest request = createRequest(data, destination);
        try {
            CloseableHttpResponse destinationResponse = httpClientProvider.getHttpClient(mapping.getName()).execute(request);
            response.setStatus(destinationResponse.getStatusLine().getStatusCode());
            stream(destinationResponse.getAllHeaders()).forEach(header -> response.addHeader(header.getName(), header.getValue()));
            handleErrors(destinationResponse);
            copy(destinationResponse.getEntity().getContent(), response.getOutputStream());
            stopTimerContext(context);
            return new ResponseData(response.getStatusCode(), response.getHeaders(), response.getBody());
        } catch (HttpStatusCodeException e) {
            if (charon.getRetrying().getRetryOn().getExceptions().contains(e.getClass())) {
                traceInterceptor.onForwardFailed(traceId, e);
                throw e;
            }
            write(e.getResponseBodyAsByteArray(), response.getOutputStream());
        } catch (Exception e) {
            stopTimerContext(context);
            traceInterceptor.onForwardFailed(traceId, e);
            throw e;
        }
    }

    protected HttpUriRequest createRequest(RequestData data, ForwardDestination destination) {
        HttpUriRequest request;
        switch (data.getMethod()) {
            case GET:
                request = new HttpGet(destination.getUri());
                break;
            case HEAD:
                request = new HttpHead(destination.getUri());
                break;
            case DELETE:
                request = new HttpDelete(destination.getUri());
                break;
            case OPTIONS:
                request = new HttpOptions(destination.getUri());
                break;
            case TRACE:
                request = new HttpTrace(destination.getUri());
                break;
            case POST:
                HttpPost post = new HttpPost(destination.getUri());
                post.setEntity(new InputStreamEntity(data.getBody()));
                request = post;
                break;
            case PUT:
                HttpPut put = new HttpPut(destination.getUri());
                put.setEntity(new InputStreamEntity(data.getBody()));
                request = put;
                break;
            case PATCH:
                HttpPatch patch = new HttpPatch(destination.getUri());
                patch.setEntity(new InputStreamEntity(data.getBody()));
                request = patch;
                break;
            default:
                throw new CharonException("Invalid request method: " + data.getMethod());
        }
        data.getHeaders().forEach((name, values) -> values.forEach(value -> request.addHeader(name, value)));
        return request;
    }

    protected void stopTimerContext(Context context) {
        if (context != null) {
            context.stop();
        }
    }

    protected String resolveMetricsName(MappingProperties mapping) {
        return name(charon.getMetrics().getNamesPrefix(), mapping.getName());
    }

    protected String stripMappingPath(String uri, MappingProperties mapping) {
        return prependIfMissing(removeStart(uri, concatContextAndMappingPaths(mapping)), "/");
    }

    protected String concatContextAndMappingPaths(MappingProperties mapping) {
        return correctUri(server.getContextPath()) + mapping.getPath();
    }

    protected void handleErrors(CloseableHttpResponse destinationResponse) throws IOException {
        HttpStatus status = getResponseStatus(destinationResponse);
        Charset responseCharset = forName("UTF-8");
        if (status.is4xxClientError()) {
            byte[] body = toByteArray(destinationResponse.getEntity().getContent());
            throw new HttpClientErrorException(status, status.getReasonPhrase(), body, responseCharset);
        }
        if (status.is5xxServerError()) {
            byte[] body = toByteArray(destinationResponse.getEntity().getContent());
            throw new HttpServerErrorException(status, status.getReasonPhrase(), body, responseCharset);
        }
    }

    protected ResponseData convertToResponseData(CloseableHttpResponse destinationResponse) {
        HttpStatus status = getResponseStatus(destinationResponse);
        HttpHeaders headers = new HttpHeaders();
        stream(destinationResponse.getAllHeaders()).forEach(new Consumer<Header>() {

            @Override
            public void accept(Header header) {
                headers.set();
            }
        });
    }

    protected HttpStatus getResponseStatus(CloseableHttpResponse destinationResponse) {
        return HttpStatus.valueOf(destinationResponse.getStatusLine().getStatusCode());
    }
}
