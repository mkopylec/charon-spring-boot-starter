package com.github.mkopylec.charon.core.trace;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import static java.util.UUID.randomUUID;

public abstract class TraceInterceptor {

    protected final ThreadLocal<String> traceId = new ThreadLocal<>();

    public void initTraceId() {
        traceId.set(randomUUID().toString());
    }

    public void onRequestReceived(HttpMethod method, String uri, byte[] body, HttpHeaders headers) {
        IncomingRequest request = new IncomingRequest()
                .setMethod(method)
                .setUri(uri)
                .setBody(body)
                .setHeaders(headers);
        onRequestReceived(traceId.get(), request);
    }

    public void onForwardStart(String mappingName, HttpMethod method, String uri, byte[] body, HttpHeaders headers) {
        ForwardRequest request = new ForwardRequest()
                .setMappingName(mappingName)
                .setMethod(method)
                .setUri(uri)
                .setBody(body)
                .setHeaders(headers);
        onForwardStart(traceId.get(), request);
    }

    public void onForwardComplete(HttpStatus status, byte[] body, HttpHeaders headers) {
        ReceivedResponse response = new ReceivedResponse()
                .setStatus(status)
                .setBody(body)
                .setHeaders(headers);
        onForwardComplete(traceId.get(), response);
    }

    public void cleanTraceId() {
        traceId.remove();
    }

    protected abstract void onRequestReceived(String traceId, IncomingRequest request);

    protected abstract void onForwardStart(String traceId, ForwardRequest request);

    protected abstract void onForwardComplete(String traceId, ReceivedResponse response);
}
