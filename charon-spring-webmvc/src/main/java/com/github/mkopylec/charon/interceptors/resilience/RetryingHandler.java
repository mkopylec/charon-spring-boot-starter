package com.github.mkopylec.charon.interceptors.resilience;

import com.github.mkopylec.charon.interceptors.HttpRequest;
import com.github.mkopylec.charon.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.interceptors.HttpResponse;
import io.github.resilience4j.retry.RetryConfig;

import static io.github.resilience4j.retry.RetryConfig.custom;
import static java.time.Duration.ofMillis;

class RetryingHandler extends ResilienceHandler<RetryConfig> {

    RetryingHandler() {
        super(custom()
                .waitDuration(ofMillis(10))
                .retryOnResult(result -> {
                    HttpResponse response = (HttpResponse) result;
                    return response.getStatusCode().is5xxServerError();
                })
                .build());
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        // TODO Implement retrying
        return null;
    }

    @Override
    public int getOrder() {
        return REQUEST_RETRYING_HANDLER_ORDER;
    }
}
