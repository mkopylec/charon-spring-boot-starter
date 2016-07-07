package com.github.mkopylec.charon.core.http;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer.Context;
import com.github.mkopylec.charon.configuration.CharonProperties;
import com.github.mkopylec.charon.configuration.CharonProperties.Mapping;
import com.github.mkopylec.charon.core.balancer.LoadBalancer;
import com.github.mkopylec.charon.core.mappings.MappingsProvider;
import com.github.mkopylec.charon.core.trace.LoggingTraceInterceptor;
import com.github.mkopylec.charon.exceptions.CharonException;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.RetryContext;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestOperations;

import java.net.URI;
import java.net.URISyntaxException;

import static com.codahale.metrics.MetricRegistry.name;
import static com.github.mkopylec.charon.configuration.CharonProperties.Retrying.MAPPING_NAME_RETRY_ATTRIBUTE;
import static com.github.mkopylec.charon.core.utils.PredicateRunner.runIfTrue;
import static com.github.mkopylec.charon.core.utils.UriCorrector.correctUri;
import static org.apache.commons.lang3.StringUtils.removeStart;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.status;

public class RequestForwarder {

    private static final Logger log = getLogger(RequestForwarder.class);

    protected final ServerProperties server;
    protected final CharonProperties charon;
    protected final RestOperations restOperations;
    protected final MappingsProvider mappingsProvider;
    protected final LoadBalancer loadBalancer;
    protected final MetricRegistry metricRegistry;
    protected final LoggingTraceInterceptor traceInterceptor;

    public RequestForwarder(
            ServerProperties server,
            CharonProperties charon,
            RestOperations restOperations,
            MappingsProvider mappingsProvider,
            LoadBalancer loadBalancer,
            MetricRegistry metricRegistry,
            LoggingTraceInterceptor traceInterceptor
    ) {
        this.server = server;
        this.charon = charon;
        this.restOperations = restOperations;
        this.mappingsProvider = mappingsProvider;
        this.loadBalancer = loadBalancer;
        this.metricRegistry = metricRegistry;
        this.traceInterceptor = traceInterceptor;
    }

    public ResponseEntity<byte[]> forwardHttpRequest(byte[] body, HttpHeaders headers, HttpMethod method, String originUri, RetryContext context) {
        ForwardDestination destination = resolveForwardDestination(originUri);
        if (destination == null) {
            runIfTrue(charon.getTrace().isEnabled(), () -> traceInterceptor.onForwardStart(null, method, originUri, body, headers));
            log.debug("Forwarding: {} {} -> no mapping found", method, originUri);
            return null;
        } else {
            runIfTrue(charon.getTrace().isEnabled(), () -> traceInterceptor.onForwardStart(destination.getMappingName(), method, originUri, body, headers));
        }
        context.setAttribute(MAPPING_NAME_RETRY_ATTRIBUTE, destination.getMappingName());
        RequestEntity<byte[]> requestEntity = new RequestEntity<>(body, headers, method, destination.getUri());
        ResponseEntity<byte[]> response = sendRequest(requestEntity, destination.getMappingMetricsName());
        runIfTrue(charon.getTrace().isEnabled(), () -> traceInterceptor.onForwardComplete(response.getStatusCode(), response.getBody(), response.getHeaders()));

        log.info("Forwarding: {} {} -> {} {}", method, originUri, destination.getUri(), response.getStatusCode().value());

        return response;
    }

    protected ForwardDestination resolveForwardDestination(String originUri) {
        Mapping mapping = mappingsProvider.resolveMapping(originUri);
        if (mapping == null) {
            return null;
        }
        return new ForwardDestination(createDestinationUrl(originUri, mapping), mapping.getName(), resolveMetricsName(mapping));
    }

    protected URI createDestinationUrl(String uri, Mapping mapping) {
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

    protected ResponseEntity<byte[]> sendRequest(RequestEntity<byte[]> requestEntity, String mappingMetricsName) {
        ResponseEntity<byte[]> responseEntity;
        Context context = null;
        if (charon.getMetrics().isEnabled()) {
            context = metricRegistry.timer(mappingMetricsName).time();
        }
        try {
            responseEntity = restOperations.exchange(requestEntity, byte[].class);
            stopTimerContext(context);
        } catch (HttpStatusCodeException e) {
            stopTimerContext(context);
            responseEntity = status(e.getStatusCode())
                    .headers(e.getResponseHeaders())
                    .body(e.getResponseBodyAsByteArray());
        } catch (Exception e) {
            mappingsProvider.updateMappingsIfAllowed();
            throw e;
        }
        return responseEntity;
    }

    protected void stopTimerContext(Context context) {
        if (context != null) {
            context.stop();
        }
    }

    protected String resolveMetricsName(Mapping mapping) {
        return name(charon.getMetrics().getNamesPrefix(), mapping.getName());
    }

    protected String concatContextAndMappingPaths(Mapping mapping) {
        return correctUri(server.getContextPath()) + mapping.getPath();
    }
}
