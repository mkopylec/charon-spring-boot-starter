package com.github.mkopylec.charon.test;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;
import reactor.core.publisher.Mono;

import static org.slf4j.LoggerFactory.getLogger;
import static reactor.core.publisher.Mono.error;

class ExceptionThrower extends CommonExceptionThrower implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(ExceptionThrower.class);

    ExceptionThrower() {
        super(log);
    }

    @Override
    public Mono<HttpResponse> forward(HttpRequest request, HttpRequestExecution execution) {
        logStart(execution.getMappingName());
        return error(createFailure());
    }
}
