package com.github.mkopylec.charon.forwarding.interceptors.log;

import java.net.URI;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;

import org.springframework.http.HttpMethod;

import static org.slf4j.LoggerFactory.getLogger;

class ForwardingLogger extends BasicForwardingLogger implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(ForwardingLogger.class);

    ForwardingLogger() {
        super(log);
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        HttpMethod oldMethod = request.getMethod();
        URI oldUri = request.getURI();
        String forwardingName = execution.getMappingName();
        try {
            HttpResponse response = execution.execute(request);
            String logMessage = "Forwarding: {} {} -> '{}' -> {} {} {}";
            if (response.getStatusCode().is5xxServerError()) {
                log(serverErrorLogLevel, logMessage, oldMethod, oldUri, forwardingName, request.getMethodValue(), request.getURI(), response.getRawStatusCode());
            } else if (response.getStatusCode().is4xxClientError()) {
                log(clientErrorLogLevel, logMessage, oldMethod, oldUri, forwardingName, request.getMethodValue(), request.getURI(), response.getRawStatusCode());
            } else {
                log(successLogLevel, logMessage, oldMethod, oldUri, forwardingName, request.getMethodValue(), request.getURI(), response.getRawStatusCode());
            }
            return response;
        } catch (RuntimeException e) {
            log(unexpectedErrorLogLevel, "Forwarding: {} {} -> '{}' -> {}", oldMethod, oldUri, forwardingName, e.getMessage());
            throw e;
        }
    }
}
