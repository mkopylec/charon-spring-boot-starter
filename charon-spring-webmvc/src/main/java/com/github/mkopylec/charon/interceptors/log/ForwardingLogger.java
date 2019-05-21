package com.github.mkopylec.charon.interceptors.log;

import java.net.URI;

import com.github.mkopylec.charon.interceptors.HttpRequest;
import com.github.mkopylec.charon.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.interceptors.HttpResponse;
import com.github.mkopylec.charon.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public class ForwardingLogger implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(ForwardingLogger.class);

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        String oldMethod = request.getMethodValue();
        URI oldUri = request.getURI();
        String forwardingName = execution.getMappingName();
        try {
            HttpResponse response = execution.execute(request);
            String logMessage = "Forwarding: {} {} -> '{}' -> {} {} {}";
            if (response.getStatusCode().is5xxServerError()) {
                log.error(logMessage, oldMethod, oldUri, forwardingName, request.getMethodValue(), request.getURI(), response.getRawStatusCode());
            } else if (response.getStatusCode().is4xxClientError()) {
                log.info(logMessage, oldMethod, oldUri, forwardingName, request.getMethodValue(), request.getURI(), response.getRawStatusCode());
            } else {
                log.debug(logMessage, oldMethod, oldUri, forwardingName, request.getMethodValue(), request.getURI(), response.getRawStatusCode());
            }
            return response;
        } catch (RuntimeException e) {
            log.error("Forwarding: {} {} -> '{}' -> Error: {}", oldMethod, oldUri, forwardingName, e.getMessage());
            throw e;
        }
    }

    @Override
    public int getOrder() {
        return LOGGER_ORDER;
    }
}
