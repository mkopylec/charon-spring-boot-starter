package com.github.mkopylec.charon.test;

import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType;
import org.slf4j.Logger;

abstract class CommonRequestBodyRewriter {

    private Logger log;

    CommonRequestBodyRewriter(Logger log) {
        this.log = log;
    }

    public RequestForwardingInterceptorType getType() {
        return new RequestForwardingInterceptorType(666);
    }

    void logStart(String mappingName) {
        log.trace("[Start] Request body rewriting for '{}' request mapping", mappingName);
    }

    void logEnd(String mappingName) {
        log.trace("[End] Request body rewriting for '{}' request mapping", mappingName);
    }
}
