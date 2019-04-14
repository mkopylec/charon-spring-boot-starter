package com.github.mkopylec.charon.interceptors.log;

import java.net.URI;

import com.github.mkopylec.charon.interceptors.HttpRequest;
import com.github.mkopylec.charon.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.interceptors.HttpResponse;
import com.github.mkopylec.charon.interceptors.RequestForwardingInterceptor;

import static org.slf4j.LoggerFactory.getLogger;

public class Logger implements RequestForwardingInterceptor {

    private static final org.slf4j.Logger log = getLogger(Logger.class);

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        String method = request.getMethodValue();
        URI originalUri = request.getOriginalUri();
        String forwardingName = execution.getMappingName();
        log.debug("Forwarding: {} {} -> '{}' -> ...", method, originalUri, forwardingName);
        try {
            HttpResponse response = execution.execute(request);
            String logMessage = "Forwarding: {} {} -> '{}' -> {} {}";
            if (response.getStatusCode().is5xxServerError()) {
                log.error(logMessage, method, originalUri, forwardingName, request.getURI(), response.getRawStatusCode());
            } else if (response.getStatusCode().is4xxClientError()) {
                log.info(logMessage, method, originalUri, forwardingName, request.getURI(), response.getRawStatusCode());
            } else {
                log.debug(logMessage, method, originalUri, forwardingName, request.getURI(), response.getRawStatusCode());
            }
            return response;
        } catch (RuntimeException e) {
            log.error("Forwarding: {} {} -> '{}' -> Error: {}", method, originalUri, forwardingName, e.getMessage());
            throw e;
        }
    }

    @Override
    public int getOrder() {
        return LOGGER_ORDER;
    }
}
