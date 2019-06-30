package com.github.mkopylec.charon.forwarding.interceptors.resilience;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import io.github.resilience4j.retry.Retry;
import org.slf4j.Logger;
import reactor.core.publisher.Mono;

import static io.github.resilience4j.reactor.retry.RetryOperator.of;
import static org.slf4j.LoggerFactory.getLogger;

class Retryer extends CommonRetryer<HttpResponse> implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(Retryer.class);

    Retryer() {
        super(result -> result.statusCode().is5xxServerError(), log);
    }

    @Override
    public Mono<HttpResponse> forward(HttpRequest request, HttpRequestExecution execution) {
        logStart(execution.getMappingName());
        Retry retry = getRegistry().retry(execution.getMappingName());
        setupMetrics(registry -> createMetrics(registry, execution.getMappingName()));
        return execution.execute(request)
                .transform(of(retry))
                .doOnSuccess(response -> logEnd(execution.getMappingName()));
    }
}
