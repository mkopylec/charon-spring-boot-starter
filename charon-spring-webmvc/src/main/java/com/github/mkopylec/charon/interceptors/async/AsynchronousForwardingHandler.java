package com.github.mkopylec.charon.interceptors.async;

import com.github.mkopylec.charon.interceptors.HttpRequest;
import com.github.mkopylec.charon.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.interceptors.HttpResponse;
import com.github.mkopylec.charon.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.ACCEPTED;

class AsynchronousForwardingHandler implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(AsynchronousForwardingHandler.class);

    private boolean enabled;
    private ThreadPool threadPool;

    AsynchronousForwardingHandler() {
        enabled = true;
        threadPool = new ThreadPool();
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        if (!enabled) {
            return execution.execute(request);
        }
        log.trace("[Start] Asynchronous execution of '{}' request mapping", execution.getMappingName());
        threadPool.execute(() -> forwardAsynchronously(request, execution));
        return getResponse();
    }

    @Override
    public int getOrder() {
        return ASYNCHRONOUS_FORWARDING_HANDLER_ORDER;
    }

    void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    void setThreadPool(ThreadPool threadPool) {
        this.threadPool = threadPool;
    }

    private void forwardAsynchronously(HttpRequest request, HttpRequestExecution execution) {
        try {
            HttpResponse response = execution.execute(request);
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

    private HttpResponse getResponse() {
        HttpResponse response = new HttpResponse();
        response.setStatus(ACCEPTED);
        return response;
    }
}
