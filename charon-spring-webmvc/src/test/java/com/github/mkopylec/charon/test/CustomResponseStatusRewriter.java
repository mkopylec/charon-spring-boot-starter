package com.github.mkopylec.charon.test;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;

import org.springframework.http.HttpStatus;

import static org.slf4j.LoggerFactory.getLogger;

class CustomResponseStatusRewriter implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(CustomResponseStatusRewriter.class);

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) { // TODO rewrite body test
        log.trace("[Start] Custom response status rewriting for '{}' request mapping", execution.getMappingName());
        HttpResponse response = execution.execute(request);
        rewriteStatus(execution, response);
        log.trace("[End] Custom response status rewriting for '{}' request mapping", execution.getMappingName());
        return response;
    }

    @Override
    public int getOrder() {
        return 666;
    }

    private void rewriteStatus(HttpRequestExecution execution, HttpResponse response) {
        HttpStatus oldStatus = response.getStatusCode();
        HttpStatus status = execution.getCustomProperty("default-custom-property");
        if (status != null) {
            response.setStatusCode(status);
        }
        status = execution.getCustomProperty("mapping-custom-property");
        if (status != null) {
            response.setStatusCode(status);
        }
        log.debug("Response status rewritten from {} to {}", oldStatus, response.getStatusCode());
    }
}
