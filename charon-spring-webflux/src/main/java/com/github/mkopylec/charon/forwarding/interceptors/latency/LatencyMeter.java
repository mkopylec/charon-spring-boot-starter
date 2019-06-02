package com.github.mkopylec.charon.forwarding.interceptors.latency;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;
import reactor.core.publisher.Mono;

import static java.lang.System.nanoTime;
import static org.slf4j.LoggerFactory.getLogger;

class LatencyMeter extends BasicLatencyMeter implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(LatencyMeter.class);

    @Override
    public Mono<HttpResponse> forward(HttpRequest request, HttpRequestExecution execution) {
        log.trace("[Start] Collect metrics of '{}' request mapping", execution.getMappingName());
        long startingTime = nanoTime();
        return execution.execute(request)
                .doFinally(signalType -> {
                    captureLatencyMetric(execution.getMappingName(), startingTime);
                    log.trace("[End] Collect metrics of '{}' request mapping", execution.getMappingName());
                });
    }
}
