package com.github.mkopylec.charon.test;

import org.slf4j.Logger;
import org.springframework.core.Ordered;

class BasicCustomConfigurationResponseRewriter implements Ordered {

    private Logger log;

    BasicCustomConfigurationResponseRewriter(Logger log) {
        this.log = log;
    }

    @Override
    public int getOrder() {
        return 666;
    }

    void logStart(String mappingName) {
        log.trace("[Start] Custom response rewriting for '{}' request mapping", mappingName);
    }

    void logEnd(String mappingName) {
        log.trace("[End] Custom response rewriting for '{}' request mapping", mappingName);
    }
}
