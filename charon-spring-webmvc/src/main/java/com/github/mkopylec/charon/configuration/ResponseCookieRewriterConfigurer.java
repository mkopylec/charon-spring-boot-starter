package com.github.mkopylec.charon.configuration;

import java.util.function.Consumer;

import com.github.mkopylec.charon.core.ResponseCookieRewriter;

public abstract class ResponseCookieRewriterConfigurer<R extends ResponseCookieRewriter> {

    private R responseCookieRewriter;

    protected ResponseCookieRewriterConfigurer(R responseCookieRewriter) {
        this.responseCookieRewriter = responseCookieRewriter;
    }

    protected void configure(Consumer<R> responseCookieRewriterUpdater) {
        responseCookieRewriterUpdater.accept(responseCookieRewriter);
    }

    R getResponseCookieRewriter() {
        return responseCookieRewriter;
    }
}
