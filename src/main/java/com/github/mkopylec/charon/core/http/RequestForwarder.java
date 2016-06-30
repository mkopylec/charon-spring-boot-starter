package com.github.mkopylec.charon.core.http;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.github.mkopylec.charon.configuration.CharonProperties;
import com.github.mkopylec.charon.core.balancer.LoadBalancer;
import com.github.mkopylec.charon.core.mappings.MappingsProvider;
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

import static com.codahale.metrics.MetricRegistry.name;
import static com.github.mkopylec.charon.configuration.CharonProperties.Retrying.MAPPING_NAME_RETRY_ATTRIBUTE;
import static com.github.mkopylec.charon.utils.UriCorrector.correctUri;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
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

    public RequestForwarder(
            ServerProperties server,
            CharonProperties charon,
            RestOperations restOperations,
            MappingsProvider mappingsProvider,
            LoadBalancer loadBalancer,
            MetricRegistry metricRegistry
    ) {
        this.server = server;
        this.charon = charon;
        this.restOperations = restOperations;
        this.mappingsProvider = mappingsProvider;
        this.loadBalancer = loadBalancer;
        this.metricRegistry = metricRegistry;
    }

    public ResponseEntity<byte[]> forwardHttpRequest(byte[] body, HttpHeaders headers, HttpMethod method, String originUri, RetryContext context) {
        ForwardDestination destination = resolveForwardDestination(originUri);
        if (destination == null) {
            log.debug("Forwarding: {} {} -> no mapping found", method, originUri);
            return null;
        }
        context.setAttribute(MAPPING_NAME_RETRY_ATTRIBUTE, destination.getMappingName());
        RequestEntity<byte[]> requestEntity = new RequestEntity<>(body, headers, method, destination.getUri());
        ResponseEntity<byte[]> result = sendRequest(requestEntity, destination.getMappingMetricsName());

        log.debug("Forwarding: {} {} -> {} {}", method, originUri, destination.getUri(), result.getStatusCode().value());

        return result;
    }

    protected ForwardDestination resolveForwardDestination(String uri) {
        List<ForwardDestination> destinations = mappingsProvider.getMappings().stream()
                .filter(mapping -> uri.startsWith(concatContextAndMappingPaths(mapping)))
                .map(mapping -> new ForwardDestination(createDestinationUrl(uri, mapping), mapping.getName(), resolveMetricsName(mapping)))
                .collect(toList());
        if (isEmpty(destinations)) {
            return null;
        }
        if (destinations.size() == 1) {
            return destinations.get(0);
        }
        throw new CharonException("Multiple mapping paths found for HTTP request URI: " + uri);
    }

    protected URI createDestinationUrl(String uri, CharonProperties.Mapping mapping) {
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
        Timer.Context context = null;
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

    protected void stopTimerContext(Timer.Context context) {
        if (context != null) {
            context.stop();
        }
    }

    protected String resolveMetricsName(CharonProperties.Mapping mapping) {
        return name(charon.getMetrics().getNamesPrefix(), mapping.getName());
    }

    protected String concatContextAndMappingPaths(CharonProperties.Mapping mapping) {
        return correctUri(server.getContextPath()) + mapping.getPath();
    }
}
