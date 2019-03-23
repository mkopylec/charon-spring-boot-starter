package com.github.mkopylec.charon.configuration;

import com.github.mkopylec.charon.core.interceptors.CopyRequestPathRewriter;

public class CopyRequestPathRewriterConfigurer extends RequestForwardingInterceptorConfigurer<CopyRequestPathRewriter> {

    protected CopyRequestPathRewriterConfigurer() {
        super(new CopyRequestPathRewriter());
    }

    public static CopyRequestPathRewriterConfigurer copyRequestPathRewriter() {
        return new CopyRequestPathRewriterConfigurer();
    }
}
