package com.github.mkopylec.charon.core.trace;

import org.slf4j.Logger;

import static org.apache.commons.lang3.StringUtils.trimToEmpty;
import static org.slf4j.LoggerFactory.getLogger;

public class LoggingTraceInterceptor implements TraceInterceptor {

    private static final Logger log = getLogger(LoggingTraceInterceptor.class);

    @Override
    public void onRequestReceived(String traceId, IncomingRequest request) {
        log.info("\n  Trace ID: {}\n  Incoming HTTP request received:\n    - method: {}\n    - uri: {}\n    - headers: {}",
                traceId, request.getMethod(), request.getUri(), request.getHeaders()
        );
    }

    @Override
    public void onNoMappingFound(String traceId, IncomingRequest request) {
        log.info("\n  Trace ID: {}\n  No mapping found for incoming HTTP request:\n    - method: {}\n    - uri: {}\n    - headers: {}",
                traceId, request.getMethod(), request.getUri(), request.getHeaders()
        );
    }

    @Override
    public void onForwardStart(String traceId, ForwardRequest request) {
        log.info("\n  Trace ID: {}\n  Forwarding HTTP request started:\n    - mapping name: {}\n    - method: {}\n    - uri: {}\n    - body: {}\n    - headers: {}",
                traceId, trimToEmpty(request.getMappingName()), request.getMethod(), request.getUri(), request.getBodyAsString(), request.getHeaders()
        );
    }

    @Override
    public void onForwardError(String traceId, Throwable error) {
        log.error("\n  Trace ID: {}\n  Forwarding HTTP request failed:\n    - message: {}\n    - stack trace: ",
                traceId, error.getMessage(), error
        );
    }

    @Override
    public void onForwardComplete(String traceId, ReceivedResponse response) {
        log.info("\n  Trace ID: {}\n  Forward HTTP response received:\n    - status: {}\n    - body: {}\n    - headers: {}",
                traceId, response.getStatus(), response.getBodyAsString(), response.getHeaders()
        );
    }
}
