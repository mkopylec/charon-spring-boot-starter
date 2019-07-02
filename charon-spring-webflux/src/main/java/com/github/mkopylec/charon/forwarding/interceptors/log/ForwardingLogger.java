package com.github.mkopylec.charon.forwarding.interceptors.log;

import java.net.URI;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpMethod;

import static org.slf4j.LoggerFactory.getLogger;

class ForwardingLogger extends CommonForwardingLogger implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(ForwardingLogger.class);

    ForwardingLogger() {
        super(log);
    }

    @Override
    public Mono<HttpResponse> forward(HttpRequest request, HttpRequestExecution execution) {
        HttpMethod originalMethod = request.method();
        URI originalUri = request.url();
        String mappingName = execution.getMappingName();
        return execution.execute(request)
                .doOnSuccess(response -> logForwardingResult(response.statusCode(), originalMethod, request.method(), originalUri, request.url(), mappingName))
                .doOnError(RuntimeException.class, e -> {
                    logForwardingError(e, originalMethod, originalUri, mappingName);
                    throw e;
                });
    }
}
