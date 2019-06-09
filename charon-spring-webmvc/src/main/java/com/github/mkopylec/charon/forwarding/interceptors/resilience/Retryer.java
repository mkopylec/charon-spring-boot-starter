package com.github.mkopylec.charon.forwarding.interceptors.resilience;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import io.github.resilience4j.retry.Retry;
import org.slf4j.Logger;

import static com.github.mkopylec.charon.forwarding.interceptors.resilience.RetryingState.clearRetryAttempts;
import static com.github.mkopylec.charon.forwarding.interceptors.resilience.RetryingState.nextRetryAttempt;
import static org.slf4j.LoggerFactory.getLogger;

class Retryer extends BasicRetryer implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(Retryer.class);

    Retryer() {
        super(result -> ((HttpResponse) result).getStatusCode().is5xxServerError(), log);
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        logStart(execution.getMappingName());
        Retry retry = registry.retry(execution.getMappingName());
        setupMetrics(registry -> createMetrics(registry, execution.getMappingName()));
        HttpResponse response;
        try {
            response = retry.executeSupplier(() -> {
                nextRetryAttempt();
                return execution.execute(request);
            });
        } finally {
            clearRetryAttempts();
        }
        logEnd(execution.getMappingName());
        return response;
    }
}
