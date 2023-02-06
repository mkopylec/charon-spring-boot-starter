package com.github.mkopylec.charon.test;

import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType;
import org.slf4j.Logger;

abstract class CommonExceptionThrower {

    private Logger log;

    CommonExceptionThrower(Logger log) {
        this.log = log;
    }

    public RequestForwardingInterceptorType getType() {
        return new RequestForwardingInterceptorType(201);
    }

    void logStart(String mappingName) {
        log.trace("[Start] Exception throwing for '{}' request mapping", mappingName);
    }

    RuntimeException createFailure() {
        return new RuntimeException("Error forwarding request");
    }
}
