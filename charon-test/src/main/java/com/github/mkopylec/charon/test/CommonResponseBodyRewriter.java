package com.github.mkopylec.charon.test;

import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType;
import org.slf4j.Logger;

abstract class CommonResponseBodyRewriter {

    private Logger log;

    CommonResponseBodyRewriter(Logger log) {
        this.log = log;
    }

    public RequestForwardingInterceptorType getType() {
        return new RequestForwardingInterceptorType(667);
    }

    void logStart(String mappingName) {
        log.trace("[Start] Response body rewriting for '{}' request mapping", mappingName);
    }

    void logEnd(String mappingName) {
        log.trace("[End] Response body rewriting for '{}' request mapping", mappingName);
    }
}
