package com.github.mkopylec.charon.forwarding.interceptors.async;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;
import reactor.core.publisher.Mono;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static reactor.core.publisher.Mono.just;

class AsynchronousForwarder extends BasicAsynchronousForwarder implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(AsynchronousForwarder.class);

    AsynchronousForwarder() {
        super(log);
    }

    @Override
    public Mono<HttpResponse> forward(HttpRequest request, HttpRequestExecution execution) {
        threadPool.execute(() -> forwardAsynchronously(request, execution));
        return just(new HttpResponse(ACCEPTED));
    }

    private void forwardAsynchronously(HttpRequest request, HttpRequestExecution execution) {
        logStart(execution.getMappingName());
        execution.execute(request)
                .doOnSuccess(response -> {
                    String logMessage = "Asynchronous execution of '{}' request mapping resulted in {} response status";
                    if (response.statusCode().is5xxServerError()) {
                        log.error(logMessage, execution.getMappingName(), response.rawStatusCode());
                    } else if (response.statusCode().is4xxClientError()) {
                        log.info(logMessage, execution.getMappingName(), response.rawStatusCode());
                    } else {
                        log.debug(logMessage, execution.getMappingName(), response.rawStatusCode());
                    }
                    logEnd(execution.getMappingName());
                })
                .doOnError(RuntimeException.class, e -> logError(execution.getMappingName(), e))
                .subscribe();
    }
}
