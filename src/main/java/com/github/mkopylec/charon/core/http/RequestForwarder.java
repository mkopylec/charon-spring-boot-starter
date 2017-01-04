package com.github.mkopylec.charon.core.http;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer.Context;
import com.github.mkopylec.charon.configuration.CharonProperties;
import com.github.mkopylec.charon.configuration.MappingProperties;
import com.github.mkopylec.charon.core.balancer.LoadBalancer;
import com.github.mkopylec.charon.core.mappings.MappingsProvider;
import com.github.mkopylec.charon.core.trace.TraceInterceptor;
import com.github.mkopylec.charon.exceptions.CharonException;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.RetryContext;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestOperations;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import static com.codahale.metrics.MetricRegistry.name;
import static com.github.mkopylec.charon.configuration.RetryingProperties.MAPPING_NAME_RETRY_ATTRIBUTE;
import static com.github.mkopylec.charon.core.utils.PredicateRunner.runIfTrue;
import static com.github.mkopylec.charon.core.utils.UriCorrector.correctUri;
import static org.apache.commons.lang3.StringUtils.removeStart;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.status;

public class RequestForwarder {

    private static final Logger log = getLogger(RequestForwarder.class);

    protected final ServerProperties server;
    protected final CharonProperties charon;
    protected final Map<String, RestOperations> restOperations;
    protected final MappingsProvider mappingsProvider;
    protected final LoadBalancer loadBalancer;
    protected final MetricRegistry metricRegistry;
    protected final TraceInterceptor traceInterceptor;

    public RequestForwarder(
            ServerProperties server,
            CharonProperties charon,
            Map<String, RestOperations> restOperations,
            MappingsProvider mappingsProvider,
            LoadBalancer loadBalancer,
            MetricRegistry metricRegistry,
            TraceInterceptor traceInterceptor
    ) {
        this.server = server;
        this.charon = charon;
        this.restOperations = restOperations;
        this.mappingsProvider = mappingsProvider;
        this.loadBalancer = loadBalancer;
        this.metricRegistry = metricRegistry;
        this.traceInterceptor = traceInterceptor;
    }

    public ResponseEntity<byte[]> forwardHttpRequest(RequestData data, String traceId, RetryContext context, MappingProperties mapping) {
        ForwardDestination destination = resolveForwardDestination(data.getUri(), mapping);
        runIfTrue(charon.getTracing().isEnabled(), () -> traceInterceptor.onForwardStart(
                traceId, destination.getMappingName(), data.getMethod(), destination.getUri().toString(), data.getBody(), data.getHeaders())
        );
        context.setAttribute(MAPPING_NAME_RETRY_ATTRIBUTE, destination.getMappingName());
        RequestEntity<byte[]> requestEntity = new RequestEntity<>(data.getBody(), data.getHeaders(), data.getMethod(), destination.getUri());
        ResponseEntity<byte[]> response = sendRequest(traceId, requestEntity, mapping, destination.getMappingMetricsName());

        log.info("Forwarding: {} {} -> {} {}", data.getMethod(), data.getUri(), destination.getUri(), response.getStatusCode().value());

        runIfTrue(charon.getTracing().isEnabled(), () -> traceInterceptor.onForwardComplete(
                traceId, response.getStatusCode(), response.getBody(), response.getHeaders())
        );

        return response;
    }

    protected ForwardDestination resolveForwardDestination(String originUri, MappingProperties mapping) {
        return new ForwardDestination(createDestinationUrl(originUri, mapping), mapping.getName(), resolveMetricsName(mapping));
    }

    protected URI createDestinationUrl(String uri, MappingProperties mapping) {
        if (mapping.isStripPath()) {
            uri = removeStart(uri, concatContextAndMappingPaths(mapping));
        }
        String host = loadBalancer.chooseDestination(mapping.getDestinations());
        try {
            return new URI(host + uri);
        } catch (URISyntaxException e) {
            throw new CharonException("Error creating destination URL from HTTP request URI: " + uri + " using mapping " + mapping, e);
        }
    }

    protected ResponseEntity<byte[]> sendRequest(String traceId, RequestEntity<byte[]> requestEntity, MappingProperties mapping, String mappingMetricsName) {
        ResponseEntity<byte[]> responseEntity;
        Context context = null;
        if (charon.getMetrics().isEnabled()) {
            context = metricRegistry.timer(mappingMetricsName).time();
        }
        try {
            responseEntity = restOperations.get(mapping.getName()).exchange(requestEntity, byte[].class);
            stopTimerContext(context);
        } catch (HttpStatusCodeException e) {
            stopTimerContext(context);
            if (charon.getRetrying().getRetryOn().getExceptions().contains(e.getClass())) {
                runIfTrue(charon.getTracing().isEnabled(), () -> traceInterceptor.onForwardFailed(traceId, e));
                throw e;
            }
            responseEntity = status(e.getStatusCode())
                    .headers(e.getResponseHeaders())
                    .body(e.getResponseBodyAsByteArray());
        } catch (Exception e) {
            stopTimerContext(context);
            runIfTrue(charon.getTracing().isEnabled(), () -> traceInterceptor.onForwardFailed(traceId, e));
            throw new CharonException("Error sending HTTP request", e);
        }
        return responseEntity;
    }

    protected void stopTimerContext(Context context) {
        if (context != null) {
            context.stop();
        }
    }

    protected String resolveMetricsName(MappingProperties mapping) {
        return name(charon.getMetrics().getNamesPrefix(), mapping.getName());
    }

    protected String concatContextAndMappingPaths(MappingProperties mapping) {
        return correctUri(server.getContextPath()) + mapping.getPath();
    }
}
