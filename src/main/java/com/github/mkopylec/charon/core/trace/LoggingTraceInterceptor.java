package com.github.mkopylec.charon.core.trace;

import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public class LoggingTraceInterceptor extends TraceInterceptor {

    private static final Logger log = getLogger(LoggingTraceInterceptor.class);

    @Override
    protected void onRequestReceived(String traceId, IncomingRequest request) {
        log.info("\n  Trace ID: {}\n  Incoming HTTP request details:\n    - method: {}\n    - uri: {}\n    - body: {}\n    - headers: {}",
                traceId, request.getMethod(), request.getUri(), request.getBody(), request.getHeaders()
        );
    }

    @Override
    protected void onForwardStart(String traceId, ForwardRequest request) {
        log.info("\n  Trace ID: {}\n  Forward HTTP request details:\n    - mapping name: {}\n    - method: {}\n    - uri: {}\n    - body: {}\n    - headers: {}",
                traceId, request.getMappingName(), request.getMethod(), request.getUri(), request.getBody(), request.getHeaders()
        );
    }

    @Override
    protected void onForwardComplete(String traceId, ReceivedResponse response) {
        log.info("\n  Trace ID: {}\n  Received forward HTTP response details:\n    - status: {}\n    - body: {}\n    - headers: {}",
                traceId, response.getStatus(), response.getBody(), response.getHeaders()
        );
    }
}
