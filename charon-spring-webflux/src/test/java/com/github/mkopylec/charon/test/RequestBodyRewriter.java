package com.github.mkopylec.charon.test;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;
import reactor.core.publisher.Mono;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

class RequestBodyRewriter extends CommonRequestBodyRewriter implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(RequestBodyRewriter.class);

    RequestBodyRewriter() {
        super(log);
    }

    @Override
    public Mono<HttpResponse> forward(HttpRequest request, HttpRequestExecution execution) {
        logStart(execution.getMappingName());
        rewriteRequest(request);
        return execution.execute(request)
                .doOnSuccess(response -> logEnd(execution.getMappingName()));
    }

    private void rewriteRequest(HttpRequest request) {
        String rewrittenBody = "Rewritten request body";
        request.setBody(fromValue(rewrittenBody));
        log.debug("Request body rewritten to '{}'", rewrittenBody);
    }
}
