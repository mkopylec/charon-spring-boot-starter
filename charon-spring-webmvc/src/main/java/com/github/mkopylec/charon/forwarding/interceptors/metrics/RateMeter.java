package com.github.mkopylec.charon.forwarding.interceptors.metrics;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

class RateMeter extends CommonRateMeter implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(RateMeter.class);

    RateMeter() {
        super(log);
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        logStart(execution.getMappingName());
        try {
            HttpResponse response = execution.execute(request);
            captureResponseStatusMetric(execution.getMappingName(), response.getStatusCode());
            return response;
        } catch (RuntimeException ex) {
            captureExceptionMetric(execution.getMappingName(), ex);
            throw ex;
        } finally {
            logEnd(execution.getMappingName());
        }
    }
}
