package com.github.mkopylec.charon.forwarding.interceptors.log;

import java.net.URI;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;

import org.springframework.http.HttpMethod;

import static org.slf4j.LoggerFactory.getLogger;

class ForwardingLogger extends CommonForwardingLogger implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(ForwardingLogger.class);

    ForwardingLogger() {
        super(log);
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        HttpMethod originalMethod = request.getMethod();
        URI originalUri = request.getURI();
        String mappingName = execution.getMappingName();
        try {
            HttpResponse response = execution.execute(request);
            logForwardingResult(response.getStatusCode(), originalMethod, request.getMethod(), originalUri, request.getURI(), mappingName);
            return response;
        } catch (RuntimeException e) {
            logForwardingError(e, originalMethod, originalUri, mappingName);
            throw e;
        }
    }
}
