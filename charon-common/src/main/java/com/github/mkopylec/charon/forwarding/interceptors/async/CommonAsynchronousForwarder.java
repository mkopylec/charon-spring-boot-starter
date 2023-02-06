package com.github.mkopylec.charon.forwarding.interceptors.async;

import com.github.mkopylec.charon.configuration.Valid;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType;
import org.slf4j.Logger;
import org.springframework.http.HttpStatusCode;

import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.ASYNCHRONOUS_FORWARDING_HANDLER;
import static com.github.mkopylec.charon.forwarding.interceptors.async.ThreadPoolConfigurer.threadPool;
import static org.springframework.http.HttpStatus.ACCEPTED;

abstract class CommonAsynchronousForwarder implements Valid {

    private Logger log;
    private ThreadPool threadPool;

    CommonAsynchronousForwarder(Logger log) {
        this.log = log;
        threadPool = threadPool().configure();
    }

    public RequestForwardingInterceptorType getType() {
        return ASYNCHRONOUS_FORWARDING_HANDLER;
    }

    ThreadPool getThreadPool() {
        return threadPool;
    }

    void setThreadPool(ThreadPool threadPool) {
        this.threadPool = threadPool;
    }

    HttpStatusCode getResponseStatus() {
        return ACCEPTED;
    }

    void logForwardingResult(HttpStatusCode responseStatus, String mappingName) {
        String logMessage = "Asynchronous execution of '{}' request mapping resulted in {} response status";
        if (responseStatus.is5xxServerError()) {
            log.error(logMessage, mappingName, responseStatus.value());
        } else if (responseStatus.is4xxClientError()) {
            log.info(logMessage, mappingName, responseStatus.value());
        } else {
            log.debug(logMessage, mappingName, responseStatus.value());
        }
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
