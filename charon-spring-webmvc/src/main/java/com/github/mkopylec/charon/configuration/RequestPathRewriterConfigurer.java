package com.github.mkopylec.charon.configuration;

import java.util.function.Consumer;

import com.github.mkopylec.charon.core.RequestPathRewriter;

public abstract class RequestPathRewriterConfigurer<R extends RequestPathRewriter> {

    private R requestPathRewriter;

    protected RequestPathRewriterConfigurer(R requestPathRewriter) {
        this.requestPathRewriter = requestPathRewriter;
    }

    protected void configure(Consumer<R> requestPathRewriterUpdater) {
        requestPathRewriterUpdater.accept(requestPathRewriter);
    }

    R getRequestPathRewriter() {
        return requestPathRewriter;
    }
}
