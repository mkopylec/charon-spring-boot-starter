package com.github.mkopylec.charon.application;

import com.github.mkopylec.charon.core.trace.ForwardRequest;
import com.github.mkopylec.charon.core.trace.IncomingRequest;
import com.github.mkopylec.charon.core.trace.ReceivedResponse;
import com.github.mkopylec.charon.core.trace.TraceInterceptor;

import org.springframework.stereotype.Component;

/**
 * This class in not thread safe.
 */
@Component
public class TestTraceInterceptor extends TraceInterceptor {

    private boolean requestReceivedCaptured = false;
    private boolean forwardStartWithMappingCaptured = false;
    private boolean forwardStartWithNoMappingCaptured = false;
    private boolean forwardCompleteCaptured = false;

    @Override
    protected void onRequestReceived(String traceId, IncomingRequest request) {
        requestReceivedCaptured = true;
    }

    @Override
    protected void onForwardStart(String traceId, ForwardRequest request) {
        forwardStartWithMappingCaptured = request.getMappingName() != null;
        forwardStartWithNoMappingCaptured = request.getMappingName() == null;
    }

    @Override
    protected void onForwardComplete(String traceId, ReceivedResponse response) {
        forwardCompleteCaptured = true;
    }

    public boolean isRequestReceivedCaptured() {
        return requestReceivedCaptured;
    }

    public boolean isForwardStartWithMappingCaptured() {
        return forwardStartWithMappingCaptured;
    }

    public boolean isForwardStartWithNoMappingCaptured() {
        return forwardStartWithNoMappingCaptured;
    }

    public boolean isForwardCompleteCaptured() {
        return forwardCompleteCaptured;
    }
}
