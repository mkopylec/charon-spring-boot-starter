package com.github.mkopylec.charon.test;

import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorConfigurer;

class CustomConfigurationResponseRewriterConfigurer extends RequestForwardingInterceptorConfigurer<CustomConfigurationResponseRewriter> {

    private CustomConfigurationResponseRewriterConfigurer() {
        super(new CustomConfigurationResponseRewriter());
    }

    static CustomConfigurationResponseRewriterConfigurer customConfigurationResponseRewriter() {
        return new CustomConfigurationResponseRewriterConfigurer();
    }
}
