package com.github.mkopylec.charon.core.http;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer.Context;
import com.github.mkopylec.charon.configuration.CharonProperties;
import com.github.mkopylec.charon.configuration.MappingProperties;
import com.github.mkopylec.charon.core.balancer.LoadBalancer;
import com.github.mkopylec.charon.core.mappings.MappingsProvider;
import com.github.mkopylec.charon.core.trace.ProxyingTraceInterceptor;
import com.github.mkopylec.charon.exceptions.CharonException;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.RetryContext;
import org.springframework.web.client.HttpStatusCodeException;

import java.net.URI;
import java.net.URISyntaxException;

import static com.codahale.metrics.MetricRegistry.name;
import static com.github.mkopylec.charon.configuration.RetryingProperties.MAPPING_NAME_RETRY_ATTRIBUTE;
import static com.github.mkopylec.charon.core.utils.UriCorrector.correctUri;
import static org.apache.commons.lang3.StringUtils.prependIfMissing;
import static org.apache.commons.lang3.StringUtils.removeStart;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpHeaders.CONNECTION;
import static org.springframework.http.HttpHeaders.HOST;
import static org.springframework.http.HttpHeaders.SERVER;
import static org.springframework.http.HttpHeaders.TE;
import static org.springframework.http.HttpHeaders.TRANSFER_ENCODING;
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
        prepareForwardedRequestHeaders(data, destination);
        traceInterceptor.onForwardStart(traceId, destination.getMappingName(), data.getMethod(), destination.getUri().toString(), data.getBody(), data.getHeaders());
        context.setAttribute(MAPPING_NAME_RETRY_ATTRIBUTE, destination.getMappingName());
        RequestEntity<byte[]> request = new RequestEntity<>(data.getBody(), data.getHeaders(), data.getMethod(), destination.getUri());
        ResponseData response = sendRequest(traceId, request, mapping, destination.getMappingMetricsName());

        log.debug("Forwarding: {} {} -> {} {}", data.getMethod(), data.getUri(), destination.getUri(), response.getStatus().value());

        traceInterceptor.onForwardComplete(traceId, response.getStatus(), response.getBody(), response.getHeaders());
        receivedResponseInterceptor.intercept(response);
        prepareForwardedResponseHeaders(response);

        return status(response.getStatus())
                .headers(response.getHeaders())
                .body(response.getBody());
    }

    /**
     * Remove any protocol-level headers from the remote server's response that
     * do not apply to the new response we are sending.
     */
    private void prepareForwardedResponseHeaders(ResponseData response) {
        HttpHeaders headers = response.getHeaders();
        headers.remove(TRANSFER_ENCODING);
        headers.remove(CONNECTION);
        headers.remove("Public-Key-Pins");
        headers.remove(SERVER);
        headers.remove("Strict-Transport-Security");
    }

    /**
     * Remove any protocol-level headers from the clients request that
     * do not apply to the new request we are sending to the remote server.
     */
    private void prepareForwardedRequestHeaders(RequestData request, ForwardDestination destination) {
        HttpHeaders headers = request.getHeaders();
        headers.set(HOST, destination.getUri().getAuthority());
        headers.remove(TE);
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

    protected ResponseData sendRequest(String traceId, RequestEntity<byte[]> request, MappingProperties mapping, String mappingMetricsName) {
        ResponseEntity<byte[]> response;
        Context context = null;
        if (charon.getMetrics().isEnabled()) {
            context = metricRegistry.timer(mappingMetricsName).time();
        }
        try {
            response = httpClientProvider.getHttpClient(mapping.getName()).exchange(request, byte[].class);
            stopTimerContext(context);
        } catch (HttpStatusCodeException e) {
            stopTimerContext(context);
            if (charon.getRetrying().getRetryOn().getExceptions().contains(e.getClass())) {
                traceInterceptor.onForwardFailed(traceId, e);
                throw e;
            }
            response = status(e.getStatusCode())
                    .headers(e.getResponseHeaders())
                    .body(e.getResponseBodyAsByteArray());
        } catch (Exception e) {
            stopTimerContext(context);
            traceInterceptor.onForwardFailed(traceId, e);
            throw e;
        }
        return new ResponseData(response.getStatusCode(), response.getHeaders(), response.getBody());
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
}
