package com.github.mkopylec.charon.core.trace;

import com.github.mkopylec.charon.configuration.CharonProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import static java.util.UUID.randomUUID;

public abstract class TraceInterceptor {

    protected final CharonProperties charon;

    public TraceInterceptor(CharonProperties charon) {
        this.charon = charon;
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
            onRequestReceived(traceId, request);
        });
    }

    public void onNoMappingFound(String traceId, HttpMethod method, String uri, HttpHeaders headers) {
        runIfTracingIsEnabled(() -> {
            IncomingRequest request = new IncomingRequest();
            request.setMethod(method);
            request.setUri(uri);
            request.setHeaders(headers);
            onNoMappingFound(traceId, request);
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
            onForwardStart(traceId, request);
        });
    }

    public void onForwardFailed(String traceId, Throwable error) {
        runIfTracingIsEnabled(() -> onForwardError(traceId, error));
    }

    public void onForwardComplete(String traceId, HttpStatus status, byte[] body, HttpHeaders headers) {
        runIfTracingIsEnabled(() -> {
            ReceivedResponse response = new ReceivedResponse();
            response.setStatus(status);
            response.setBody(body);
            response.setHeaders(headers);
            onForwardComplete(traceId, response);
        });
    }

    protected void runIfTracingIsEnabled(Runnable operation) {
        if (charon.getTracing().isEnabled()) {
            operation.run();
        }
    }

    protected abstract void onRequestReceived(String traceId, IncomingRequest request);

    protected abstract void onNoMappingFound(String traceId, IncomingRequest request);

    protected abstract void onForwardStart(String traceId, ForwardRequest request);

    protected abstract void onForwardError(String traceId, Throwable error);

    protected abstract void onForwardComplete(String traceId, ReceivedResponse response);
}
