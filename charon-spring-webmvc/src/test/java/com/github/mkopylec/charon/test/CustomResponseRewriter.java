package com.github.mkopylec.charon.test;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;

import org.springframework.http.HttpStatus;

import static org.slf4j.LoggerFactory.getLogger;

class CustomResponseRewriter implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(CustomResponseRewriter.class);

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        log.trace("[Start] Custom response rewriting for '{}' request mapping", execution.getMappingName());
        HttpResponse response = execution.execute(request);
        rewriteResponse(execution, response);
        log.trace("[End] Custom response rewriting for '{}' request mapping", execution.getMappingName());
        return response;
    }

    @Override
    public int getOrder() {
        return 666;
    }

    private void rewriteResponse(HttpRequestExecution execution, HttpResponse response) {
        HttpStatus oldStatus = response.getStatusCode();
        String oldBody = new String(response.getBodyAsBytes());
        HttpStatus status = execution.getCustomProperty("default-custom-property");
        rewriteResponseIfStatusIsPresent(response, status);
        status = execution.getCustomProperty("mapping-custom-property");
        rewriteResponseIfStatusIsPresent(response, status);
        log.debug("Response status rewritten from {} to {} and body rewritten from '{}' to '{}'", oldStatus.value(), response.getStatusCode().value(), oldBody, new String(response.getBodyAsBytes()));
    }

    private void rewriteResponseIfStatusIsPresent(HttpResponse response, HttpStatus status) {
        if (status != null) {
            response.setStatusCode(status);
            response.setBody(status.toString().getBytes());
        }
    }
}
