package com.github.mkopylec.charon.forwarding.interceptors.async;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.ACCEPTED;

class AsynchronousForwarder extends BasicAsynchronousForwarder implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(AsynchronousForwarder.class);

    AsynchronousForwarder() {
        super(log);
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        threadPool.execute(() -> forwardAsynchronously(request, execution));
        return new HttpResponse(ACCEPTED);
    }

    private void forwardAsynchronously(HttpRequest request, HttpRequestExecution execution) {
        logStart(execution.getMappingName());
        try {
            HttpResponse response = execution.execute(request);
            logForwardingResult(response.getStatusCode(), execution.getMappingName());
        } catch (Exception e) {
            logError(execution.getMappingName(), e);
        }
        logEnd(execution.getMappingName());
    }
}
