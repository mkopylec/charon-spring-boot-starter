package com.github.mkopylec.charon.test;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

class ExceptionThrower implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(ExceptionThrower.class);

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        log.trace("[Start] Exception throwing for '{}' request mapping", execution.getMappingName());
        execution.execute(request);
        throw new RuntimeException("Unexpected error has occurred");
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
