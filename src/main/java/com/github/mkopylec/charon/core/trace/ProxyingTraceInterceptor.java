package com.github.mkopylec.charon.core.trace;

import com.github.mkopylec.charon.configuration.CharonProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import static java.util.UUID.randomUUID;

public class ProxyingTraceInterceptor {

    protected final CharonProperties charon;
    protected final TraceInterceptor traceInterceptor;

    public ProxyingTraceInterceptor(CharonProperties charon, TraceInterceptor traceInterceptor) {
        this.charon = charon;
        this.traceInterceptor = traceInterceptor;
    }

    public String generateTraceId() {
        return charon.getTracing().isEnabled() ? randomUUID().toString() : null;
    }

    public void onRequestReceived(String traceId, HttpMethod method, String uri, HttpHeaders headers) {
        runIfTracingIsEnabled(() -> {
            IncomingRequest request = new IncomingRequest();
            request.setMethod(method);
            request.setUri(uri);
            request.setHeaders(headers);
            traceInterceptor.onRequestReceived(traceId, request);
        });
    }

    public void onNoMappingFound(String traceId, HttpMethod method, String uri, HttpHeaders headers) {
        runIfTracingIsEnabled(() -> {
            IncomingRequest request = new IncomingRequest();
            request.setMethod(method);
            request.setUri(uri);
            request.setHeaders(headers);
            traceInterceptor.onNoMappingFound(traceId, request);
        });
    }

    public void onForwardStart(String traceId, String mappingName, HttpMethod method, String uri, byte[] body, HttpHeaders headers) {
        runIfTracingIsEnabled(() -> {
            ForwardRequest request = new ForwardRequest();
            request.setMappingName(mappingName);
            request.setMethod(method);
            request.setUri(uri);
            request.setBody(body);
            request.setHeaders(headers);
            traceInterceptor.onForwardStart(traceId, request);
        });
    }

    public void onForwardFailed(String traceId, Throwable error) {
        runIfTracingIsEnabled(() -> traceInterceptor.onForwardError(traceId, error));
    }

    public void onForwardComplete(String traceId, HttpStatus status, byte[] body, HttpHeaders headers) {
        runIfTracingIsEnabled(() -> {
            ReceivedResponse response = new ReceivedResponse();
            response.setStatus(status);
            response.setBody(body);
            response.setHeaders(headers);
            traceInterceptor.onForwardComplete(traceId, response);
        });
    }

    protected void runIfTracingIsEnabled(Runnable operation) {
        if (charon.getTracing().isEnabled()) {
            operation.run();
        }
    }
}
