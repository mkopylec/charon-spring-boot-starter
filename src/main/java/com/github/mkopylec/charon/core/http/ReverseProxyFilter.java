package com.github.mkopylec.charon.core.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.mkopylec.charon.configuration.CharonProperties;
import com.github.mkopylec.charon.configuration.CharonProperties.Mapping;
import com.github.mkopylec.charon.core.mappings.MappingsProvider;
import com.github.mkopylec.charon.core.trace.TraceInterceptor;
import com.github.mkopylec.charon.exceptions.CharonException;
import org.slf4j.Logger;

import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.RetryOperations;
import org.springframework.web.filter.OncePerRequestFilter;

import static com.github.mkopylec.charon.core.utils.PredicateRunner.runIfTrue;
import static java.lang.String.valueOf;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.ACCEPTED;

public class ReverseProxyFilter extends OncePerRequestFilter {

    protected static final String X_FORWARDED_FOR_HEADER = "X-Forwarded-For";
    protected static final String X_FORWARDED_PROTO_HEADER = "X-Forwarded-Proto";
    protected static final String X_FORWARDED_HOST_HEADER = "X-Forwarded-Host";
    protected static final String X_FORWARDED_PORT_HEADER = "X-Forwarded-Port";

    private static final Logger log = getLogger(ReverseProxyFilter.class);

    protected final CharonProperties charon;
    protected final RetryOperations retryOperations;
    protected final RequestDataExtractor extractor;
    protected final MappingsProvider mappingsProvider;
    protected final TaskExecutor taskExecutor;
    protected final RequestForwarder requestForwarder;
    protected final TraceInterceptor traceInterceptor;

    public ReverseProxyFilter(
            CharonProperties charon,
            RetryOperations retryOperations,
            RequestDataExtractor extractor,
            MappingsProvider mappingsProvider,
            TaskExecutor taskExecutor,
            RequestForwarder requestForwarder,
            TraceInterceptor traceInterceptor
    ) {
        this.charon = charon;
        this.retryOperations = retryOperations;
        this.extractor = extractor;
        this.mappingsProvider = mappingsProvider;
        this.taskExecutor = taskExecutor;
        this.requestForwarder = requestForwarder;
        this.traceInterceptor = traceInterceptor;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            runIfTrue(charon.getTracing().isEnabled(), () -> traceInterceptor.initTraceId());
            String originUri = extractor.extractUri(request);

            log.debug("Incoming: {} {}", request.getMethod(), originUri);

            byte[] body = extractor.extractBody(request);
            HttpHeaders headers = extractor.extractHttpHeaders(request);
            HttpMethod method = extractor.extractHttpMethod(request);
            runIfTrue(charon.getTracing().isEnabled(), () -> traceInterceptor.onRequestReceived(method, originUri, body, headers));
            addForwardHeaders(request, headers);
            ResponseEntity<byte[]> responseEntity;
            if (isMappingAsynchronous(originUri)) {
                taskExecutor.execute(() -> retryOperations.execute(
                        context -> requestForwarder.forwardHttpRequest(body, headers, method, originUri, context)
                ));
                responseEntity = new ResponseEntity<>(ACCEPTED);
            } else {
                responseEntity = retryOperations.execute(context -> requestForwarder.forwardHttpRequest(body, headers, method, originUri, context));
            }
            if (responseEntity == null) {
                filterChain.doFilter(request, response);
                if (response.getStatus() == SC_NOT_FOUND) {
                    mappingsProvider.updateMappingsIfAllowed();
                }
            } else {
                processResponse(response, responseEntity);
            }
        } finally {
            runIfTrue(charon.getTracing().isEnabled(), () -> traceInterceptor.cleanTraceId());
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

    protected boolean isMappingAsynchronous(String originUri) {
        Mapping mapping = mappingsProvider.resolveMapping(originUri);
        return mapping != null && mapping.isAsynchronous();
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
