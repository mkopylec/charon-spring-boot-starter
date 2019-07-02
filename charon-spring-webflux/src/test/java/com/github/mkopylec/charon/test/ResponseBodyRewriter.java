package com.github.mkopylec.charon.test;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;
import reactor.core.publisher.Mono;

import static org.slf4j.LoggerFactory.getLogger;
import static reactor.core.publisher.Mono.defer;
import static reactor.core.publisher.Mono.just;

class ResponseBodyRewriter extends CommonResponseBodyRewriter implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(ResponseBodyRewriter.class);

    ResponseBodyRewriter() {
        super(log);
    }

    @Override
    public Mono<HttpResponse> forward(HttpRequest request, HttpRequestExecution execution) {
        logStart(execution.getMappingName());
        return execution.execute(request)
                .flatMap(this::rewriteResponse)
                .doOnSuccess(response -> logEnd(execution.getMappingName()));
    }

    private Mono<HttpResponse> rewriteResponse(HttpResponse response) {
        return response.getBody()
                .flatMap(body -> {
                    String oldBody = new String(body);
                    return rewriteResponse(response, oldBody);
                })
                .switchIfEmpty(defer(() -> rewriteResponse(response, "")));
    }

    private Mono<HttpResponse> rewriteResponse(HttpResponse response, String oldBody) {
        response.setBody("Rewritten response body".getBytes());
        return response.getBody()
                .doOnSuccess(rewrittenBody -> log.debug("Response body rewritten from '{}' to '{}'", oldBody, new String(rewrittenBody)))
                .then(just(response));
    }
}
