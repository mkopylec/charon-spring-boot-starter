package com.github.mkopylec.charon.application;

import com.github.mkopylec.charon.core.trace.ForwardRequest;
import com.github.mkopylec.charon.core.trace.IncomingRequest;
import com.github.mkopylec.charon.core.trace.ReceivedResponse;
import com.github.mkopylec.charon.core.trace.TraceInterceptor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This class in not thread safe.
 */
@Component
@ConditionalOnProperty("test.trace-interceptor-enabled")
public class TestTraceInterceptor extends TraceInterceptor {

    private boolean requestReceivedCaptured = false;
    private boolean forwardStartWithMappingCaptured = false;
    private boolean forwardStartWithNoMappingCaptured = false;
    private boolean forwardCompleteCaptured = false;
    private boolean hasUnchangeableTraceId = false;
    private String traceId = EMPTY;

    @Override
    protected void onRequestReceived(String traceId, IncomingRequest request) {
        hasUnchangeableTraceId = isNotBlank(traceId);
        this.traceId = traceId;
        requestReceivedCaptured = true;
    }

    @Override
    protected void onForwardStart(String traceId, ForwardRequest request) {
        hasUnchangeableTraceId = hasUnchangeableTraceId && this.traceId.equals(traceId);
        forwardStartWithMappingCaptured = request.getMappingName() != null;
        forwardStartWithNoMappingCaptured = request.getMappingName() == null;
    }

    @Override
    protected void onForwardComplete(String traceId, ReceivedResponse response) {
        hasUnchangeableTraceId = hasUnchangeableTraceId && this.traceId.equals(traceId);
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

    public boolean hasUnchangeableTraceId() {
        return hasUnchangeableTraceId;
    }
}
