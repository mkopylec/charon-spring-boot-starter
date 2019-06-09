package com.github.mkopylec.charon.test;

import org.slf4j.Logger;

import org.springframework.core.Ordered;

class BasicExceptionThrower implements Ordered {

    private Logger log;

    BasicExceptionThrower(Logger log) {
        this.log = log;
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

    void logStart(String mappingName) {
        log.trace("[Start] Exception throwing for '{}' request mapping", mappingName);
    }
}
