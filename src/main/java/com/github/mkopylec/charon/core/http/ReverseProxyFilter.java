package com.github.mkopylec.charon.core.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.mkopylec.charon.core.mappings.MappingsProvider;
import com.github.mkopylec.charon.exceptions.CharonException;
import org.slf4j.Logger;

import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.RetryOperations;
import org.springframework.web.filter.OncePerRequestFilter;

import static java.lang.String.valueOf;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.slf4j.LoggerFactory.getLogger;

public class ReverseProxyFilter extends OncePerRequestFilter {

    protected static final String X_FORWARDED_FOR_HEADER = "X-Forwarded-For";
    protected static final String X_FORWARDED_PROTO_HEADER = "X-Forwarded-Proto";
    protected static final String X_FORWARDED_HOST_HEADER = "X-Forwarded-Host";
    protected static final String X_FORWARDED_PORT_HEADER = "X-Forwarded-Port";

    private static final Logger log = getLogger(ReverseProxyFilter.class);

    protected final RetryOperations retryOperations;
    protected final RequestDataExtractor extractor;
    protected final MappingsProvider mappingsProvider;
    protected final TaskExecutor taskExecutor;
    protected final RequestForwarder requestForwarder;

    public ReverseProxyFilter(
            RetryOperations retryOperations,
            RequestDataExtractor extractor,
            MappingsProvider mappingsProvider,
            TaskExecutor taskExecutor,
            RequestForwarder requestForwarder
    ) {
        this.retryOperations = retryOperations;
        this.extractor = extractor;
        this.mappingsProvider = mappingsProvider;
        this.taskExecutor = taskExecutor;
        this.requestForwarder = requestForwarder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String originUri = extractor.extractUri(request);

        log.debug("Incoming: {} {}", request.getMethod(), originUri);

        byte[] body = extractor.extractBody(request);
        HttpHeaders headers = extractor.extractHttpHeaders(request);
        addForwardHeaders(request, headers);
        HttpMethod method = extractor.extractHttpMethod(request);
        ResponseEntity<byte[]> responseEntity = retryOperations.execute(
                context -> requestForwarder.forwardHttpRequest(body, headers, method, originUri, context)
        );
        if (responseEntity == null) {
            filterChain.doFilter(request, response);
            if (response.getStatus() == SC_NOT_FOUND) {
                mappingsProvider.updateMappingsIfAllowed();
            }
        } else {
            processResponse(response, responseEntity);
        }
    }

    protected void addForwardHeaders(HttpServletRequest request, HttpHeaders headers) {
        List<String> forwardedFor = headers.get(X_FORWARDED_FOR_HEADER);
        if (isEmpty(forwardedFor)) {
            forwardedFor = new ArrayList<>(1);
        }
        forwardedFor.add(request.getRemoteAddr());
        headers.put(X_FORWARDED_FOR_HEADER, forwardedFor);
        headers.set(X_FORWARDED_PROTO_HEADER, request.getScheme());
        headers.set(X_FORWARDED_HOST_HEADER, request.getServerName());
        headers.set(X_FORWARDED_PORT_HEADER, valueOf(request.getServerPort()));
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
                throw new CharonException("Error extracting body of HTTP response with status: " + responseEntity.getStatusCode(), e);
            }
        }
    }
}
