package com.github.mkopylec.reverseproxy.core.http;

import com.github.mkopylec.reverseproxy.configuration.ReverseProxyProperties;
import com.github.mkopylec.reverseproxy.configuration.ReverseProxyProperties.Mapping;
import com.github.mkopylec.reverseproxy.core.balancer.LoadBalancer;
import com.github.mkopylec.reverseproxy.core.mappings.MappingsProvider;
import com.github.mkopylec.reverseproxy.exceptions.ReverseProxyException;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.RetryOperations;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static com.github.mkopylec.reverseproxy.utils.UriCorrector.correctUri;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.removeStart;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.status;

public class HttpProxyFilter extends OncePerRequestFilter {

    protected static final String X_FORWARDED_FOR_HEADER = "X-Forwarded-For";

    private static final Logger log = getLogger(HttpProxyFilter.class);

    protected final ServerProperties server;
    protected final ReverseProxyProperties reverseProxy;
    protected final RestOperations restOperations;
    protected final RetryOperations retryOperations;
    protected final RequestDataExtractor extractor;
    protected final MappingsProvider mappingsProvider;
    protected final LoadBalancer loadBalancer;

    public HttpProxyFilter(
            ServerProperties server,
            ReverseProxyProperties reverseProxy,
            RestOperations restOperations,
            RetryOperations retryOperations,
            RequestDataExtractor extractor,
            MappingsProvider mappingsProvider,
            LoadBalancer loadBalancer
    ) {
        this.server = server;
        this.reverseProxy = reverseProxy;
        this.restOperations = restOperations;
        this.retryOperations = retryOperations;
        this.extractor = extractor;
        this.mappingsProvider = mappingsProvider;
        this.loadBalancer = loadBalancer;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = extractor.extractUri(request);

        log.debug("Incoming: {} {}", request.getMethod(), uri);

        byte[] body = extractor.extractBody(request);
        HttpHeaders headers = extractor.extractHttpHeaders(request);
        addClientIp(request, headers);
        HttpMethod method = extractor.extractHttpMethod(request);

        ResponseEntity<byte[]> responseEntity = retryOperations.execute(context -> {
            URI url = resolveDestinationUrl(uri);
            RequestEntity<byte[]> requestEntity = new RequestEntity<>(body, headers, method, url);
            ResponseEntity<byte[]> result = sendRequest(requestEntity);

            log.debug("Forwarding: {} {} -> {} {}", request.getMethod(), uri, url, result.getStatusCode().value());

            return result;
        });
        processResponse(response, responseEntity);
    }

    protected URI resolveDestinationUrl(String uri) {
        List<URI> urls = mappingsProvider.getMappings().stream()
                .filter(mapping -> uri.startsWith(concatContextAndMappingPaths(mapping)))
                .map(mapping -> createDestinationUrl(uri, mapping))
                .collect(toList());
        if (isEmpty(urls)) {
            throw new ReverseProxyException("No mapping found for HTTP request URI: " + uri);
        }
        if (urls.size() == 1) {
            return urls.get(0);
        }
        throw new ReverseProxyException("Multiple mapping paths found for HTTP request URI: " + uri);
    }

    protected URI createDestinationUrl(String uri, Mapping mapping) {
        if (mapping.isStripPath()) {
            uri = removeStart(uri, concatContextAndMappingPaths(mapping));
        }
        String host = loadBalancer.chooseDestination(mapping.getDestinations());
        try {
            return new URI(host + uri);
        } catch (URISyntaxException e) {
            throw new ReverseProxyException("Error creating destination URL from HTTP request URI: " + uri + " using mapping " + mapping, e);
        }
    }

    protected void addClientIp(HttpServletRequest request, HttpHeaders headers) {
        List<String> clientIps = headers.get(X_FORWARDED_FOR_HEADER);
        if (isEmpty(clientIps)) {
            clientIps = new ArrayList<>(1);
        }
        clientIps.add(request.getRemoteAddr());
        headers.put(X_FORWARDED_FOR_HEADER, clientIps);
    }

    protected ResponseEntity<byte[]> sendRequest(RequestEntity<byte[]> requestEntity) {
        ResponseEntity<byte[]> responseEntity;
        try {
            responseEntity = restOperations.exchange(requestEntity, byte[].class);
        } catch (HttpStatusCodeException e) {
            responseEntity = status(e.getStatusCode())
                    .headers(e.getResponseHeaders())
                    .body(e.getResponseBodyAsByteArray());
        } catch (Exception e) {
            if (shouldUpdateMappingsAfterError()) {
                mappingsProvider.updateMappings();
            }
            throw e;
        }
        return responseEntity;
    }

    protected void processResponse(HttpServletResponse response, ResponseEntity<byte[]> responseEntity) {
        response.setStatus(responseEntity.getStatusCode().value());
        responseEntity.getHeaders().forEach((name, values) ->
                values.forEach(value -> response.addHeader(name, value))
        );
        if (responseEntity.getBody() != null) {
            try {
                response.getOutputStream().write(responseEntity.getBody());
            } catch (IOException e) {
                throw new ReverseProxyException("Error extracting body of HTTP response with status: " + responseEntity.getStatusCode(), e);
            }
        }
    }

    protected String concatContextAndMappingPaths(Mapping mapping) {
        return correctUri(server.getContextPath()) + mapping.getPath();
    }

    protected boolean shouldUpdateMappingsAfterError() {
        return reverseProxy.getMappingsUpdate().isEnabled() && reverseProxy.getMappingsUpdate().isOnNonHttpError();
    }
}
