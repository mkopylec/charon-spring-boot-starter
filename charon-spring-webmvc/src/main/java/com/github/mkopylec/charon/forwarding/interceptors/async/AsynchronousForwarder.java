package com.github.mkopylec.charon.forwarding.interceptors.async;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;

import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.ASYNCHRONOUS_FORWARDING_HANDLER;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.ACCEPTED;

class AsynchronousForwarder implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(AsynchronousForwarder.class);

    private ThreadPool threadPool;

    AsynchronousForwarder() {
        threadPool = new ThreadPool();
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        threadPool.execute(() -> forwardAsynchronously(request, execution));
        return new HttpResponse(ACCEPTED);
    }

    @Override
    public int getOrder() {
        return ASYNCHRONOUS_FORWARDING_HANDLER.getOrder();
    }

    void setThreadPool(ThreadPool threadPool) {
        this.threadPool = threadPool;
    }

    private void forwardAsynchronously(HttpRequest request, HttpRequestExecution execution) {
        log.trace("[Start] Asynchronous execution of '{}' request mapping", execution.getMappingName());
        try (HttpResponse response = execution.execute(request)) {
            String logMessage = "Asynchronous execution of '{}' request mapping resulted in {} response status";
            if (response.getStatusCode().is5xxServerError()) {
                log.error(logMessage, execution.getMappingName(), response.getRawStatusCode());
            } else if (response.getStatusCode().is4xxClientError()) {
                log.info(logMessage, execution.getMappingName(), response.getRawStatusCode());
            } else {
                log.debug(logMessage, execution.getMappingName(), response.getRawStatusCode());
            }
        } catch (RuntimeException e) {
            log.error("Error executing '{}' request mapping asynchronously", execution.getMappingName(), e);
        }
        log.trace("[End] Asynchronous execution of '{}' request mapping", execution.getMappingName());
    }
}
