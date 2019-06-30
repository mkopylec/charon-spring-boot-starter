package com.github.mkopylec.charon.forwarding.interceptors.metrics;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;
import reactor.core.publisher.Mono;

import static org.slf4j.LoggerFactory.getLogger;

class RateMeter extends CommonRateMeter implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(RateMeter.class);

    RateMeter() {
        super(log);
    }

    @Override
    public Mono<HttpResponse> forward(HttpRequest request, HttpRequestExecution execution) {
        logStart(execution.getMappingName());
        return execution.execute(request)
                .doOnSuccess(response -> captureResponseStatusMetric(execution.getMappingName(), response.statusCode()))
                .doOnError(ex -> captureExceptionMetric(execution.getMappingName(), ex))
                .doFinally(signalType -> logEnd(execution.getMappingName()));
    }
}
