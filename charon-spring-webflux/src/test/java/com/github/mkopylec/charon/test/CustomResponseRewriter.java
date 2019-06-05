package com.github.mkopylec.charon.test;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;

import static org.slf4j.LoggerFactory.getLogger;
import static reactor.core.publisher.Mono.defer;
import static reactor.core.publisher.Mono.just;

class CustomResponseRewriter extends BasicCustomResponseRewriter implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(CustomResponseRewriter.class);

    CustomResponseRewriter() {
        super(log);
    }

    @Override
    public Mono<HttpResponse> forward(HttpRequest request, HttpRequestExecution execution) {
        logStart(execution.getMappingName());
        return execution.execute(request)
                .flatMap(response -> rewriteResponse(execution, response))
                .doOnSuccess(response -> logEnd(execution.getMappingName()));
    }

    private Mono<HttpResponse> rewriteResponse(HttpRequestExecution execution, HttpResponse response) {
        return response.getBody()
                .flatMap(body -> {
                    String oldBody = new String(body);
                    return rewriteResponse(execution, response, oldBody);
                })
                .switchIfEmpty(defer(() -> rewriteResponse(execution, response, "")));
    }

    private Mono<HttpResponse> rewriteResponse(HttpRequestExecution execution, HttpResponse response, String oldBody) {
        HttpStatus oldStatus = response.statusCode();
        HttpStatus status = execution.getCustomProperty("default-custom-property");
        rewriteResponseIfStatusIsPresent(response, status);
        status = execution.getCustomProperty("mapping-custom-property");
        rewriteResponseIfStatusIsPresent(response, status);
        return response.getBody()
                .doOnSuccess(rewrittenBody -> log.debug("Response status rewritten from {} to {} and body rewritten from '{}' to '{}'", oldStatus.value(), response.statusCode().value(), oldBody, new String(rewrittenBody)))
                .then(just(response));
    }

    private void rewriteResponseIfStatusIsPresent(HttpResponse response, HttpStatus status) {
        if (status != null) {
            response.setStatusCode(status);
            response.setBody(status.toString().getBytes());
        }
    }
}
