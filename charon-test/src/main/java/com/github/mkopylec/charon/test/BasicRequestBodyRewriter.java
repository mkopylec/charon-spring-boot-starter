package com.github.mkopylec.charon.test;

import org.slf4j.Logger;
import org.springframework.core.Ordered;

class BasicRequestBodyRewriter implements Ordered {

    private Logger log;

    BasicRequestBodyRewriter(Logger log) {
        this.log = log;
    }

    @Override
    public int getOrder() {
        return 666;
    }

    void logStart(String mappingName) {
        log.trace("[Start] Request body rewriting for '{}' request mapping", mappingName);
    }

    void logEnd(String mappingName) {
        log.trace("[End] Request body rewriting for '{}' request mapping", mappingName);
    }
}
