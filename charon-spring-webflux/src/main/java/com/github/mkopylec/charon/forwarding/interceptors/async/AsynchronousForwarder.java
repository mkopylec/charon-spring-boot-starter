package com.github.mkopylec.charon.forwarding.interceptors.async;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;
import reactor.core.publisher.Mono;

import static org.slf4j.LoggerFactory.getLogger;
import static reactor.core.publisher.Mono.just;

class AsynchronousForwarder extends CommonAsynchronousForwarder implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(AsynchronousForwarder.class);

    AsynchronousForwarder() {
        super(log);
    }

    @Override
    public Mono<HttpResponse> forward(HttpRequest request, HttpRequestExecution execution) {
        getThreadPool().execute(() -> forwardAsynchronously(request, execution));
        return just(new HttpResponse(getResponseStatus()));
    }

    private void forwardAsynchronously(HttpRequest request, HttpRequestExecution execution) {
        logStart(execution.getMappingName());
        execution.execute(request)
                .doOnSuccess(response -> logForwardingResult(response.statusCode(), execution.getMappingName()))
                .doOnError(Exception.class, e -> logError(execution.getMappingName(), e))
                .doFinally(signalType -> logEnd(execution.getMappingName()))
                .subscribe();
    }
}
