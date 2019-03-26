package com.github.mkopylec.charon.interceptors.rewrite;

import com.github.mkopylec.charon.interceptors.RequestForwardingInterceptorConfigurer;

public class CopyRequestPathRewriterConfigurer extends RequestForwardingInterceptorConfigurer<CopyRequestPathRewriter> {

    private CopyRequestPathRewriterConfigurer() {
        super(new CopyRequestPathRewriter());
    }

    public static CopyRequestPathRewriterConfigurer copyRequestPathRewriter() {
        return new CopyRequestPathRewriterConfigurer();
    }
}
