package com.github.mkopylec.charon.interceptors.resilience;

import com.github.mkopylec.charon.interceptors.HttpRequest;
import com.github.mkopylec.charon.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.interceptors.HttpResponse;
import io.github.resilience4j.micrometer.RetryMetrics;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import org.slf4j.Logger;

import static io.github.resilience4j.micrometer.RetryMetrics.ofRetryRegistry;
import static io.github.resilience4j.retry.RetryConfig.custom;
import static io.github.resilience4j.retry.RetryRegistry.of;
import static java.time.Duration.ofMillis;
import static org.slf4j.LoggerFactory.getLogger;

class RetryingHandler extends ResilienceHandler<RetryRegistry> {

    private static final Logger log = getLogger(RetryingHandler.class);

    RetryingHandler() {
        super(of(custom()
                .waitDuration(ofMillis(10))
                .retryOnResult(result -> ((HttpResponse) result).getStatusCode().is5xxServerError())
                .build()));
    }

    @Override
    protected HttpResponse forwardRequest(HttpRequest request, HttpRequestExecution execution) {
        log.trace("[Start] Retrying of '{}' request mapping", execution.getMappingName());
        Retry retry = registry.retry(execution.getMappingName());
        setupMetrics(this::createMetrics);
        HttpResponse response = retry.executeSupplier(() -> execution.execute(request));
        log.trace("[End] Retrying of '{}' request mapping", execution.getMappingName());
        return response;
    }

    @Override
    public int getOrder() {
        return RETRYING_HANDLER_ORDER;
    }

    private RetryMetrics createMetrics(RetryRegistry registry) {
        // TODO Wait for 0.14.0 version to be able to customize metric names
        return ofRetryRegistry(registry);
    }
}
