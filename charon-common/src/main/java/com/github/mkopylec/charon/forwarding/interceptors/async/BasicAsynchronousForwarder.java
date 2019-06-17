package com.github.mkopylec.charon.forwarding.interceptors.async;

import com.github.mkopylec.charon.configuration.Valid;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType;
import org.slf4j.Logger;

import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.ASYNCHRONOUS_FORWARDING_HANDLER;

abstract class BasicAsynchronousForwarder implements Valid {

    private Logger log;
    ThreadPool threadPool;

    BasicAsynchronousForwarder(Logger log) {
        this.log = log;
        threadPool = new ThreadPool();
    }

    public RequestForwardingInterceptorType getType() {
        return ASYNCHRONOUS_FORWARDING_HANDLER;
    }

    void setThreadPool(ThreadPool threadPool) {
        this.threadPool = threadPool;
    }

    void logStart(String mappingName) {
        log.trace("[Start] Asynchronous execution of '{}' request mapping", mappingName);
    }

    void logEnd(String mappingName) {
        log.trace("[End] Asynchronous execution of '{}' request mapping", mappingName);
    }

    void logError(String mappingName, Exception e) {
        log.error("Error executing '{}' request mapping asynchronously", mappingName, e);
    }
}
