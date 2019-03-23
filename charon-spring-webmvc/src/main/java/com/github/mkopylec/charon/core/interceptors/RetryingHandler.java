package com.github.mkopylec.charon.core.interceptors;

import com.github.mkopylec.charon.core.HttpRequest;
import com.github.mkopylec.charon.core.HttpResponse;
import com.github.mkopylec.charon.core.RequestForwarder;
import com.github.mkopylec.charon.core.RequestForwarding;
import io.github.resilience4j.retry.RetryConfig;

import org.springframework.web.client.HttpClientErrorException;

import static io.github.resilience4j.retry.RetryConfig.custom;
import static java.time.Duration.ofMillis;

public class RetryingHandler implements RequestForwardingInterceptor {

    protected boolean enabled;
    protected RetryConfig retryConfig;
    protected boolean measured;

    public RetryingHandler() {
        enabled = true;
        retryConfig = custom()
                .waitDuration(ofMillis(10))
                .ignoreExceptions(HttpClientErrorException.class)
                .build();
    }

    @Override
    public HttpResponse forward(HttpRequest request, RequestForwarding forwarding, RequestForwarder forwarder) {
        // TODO Implement retrying
        return null;
    }

    @Override
    public int getOrder() {
        return REQUEST_RETRYING_HANDLER_ORDER;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setRetryConfig(RetryConfig retryConfig) {
        this.retryConfig = retryConfig;
    }

    public void setMeasured(boolean measured) {
        this.measured = measured;
    }
}
