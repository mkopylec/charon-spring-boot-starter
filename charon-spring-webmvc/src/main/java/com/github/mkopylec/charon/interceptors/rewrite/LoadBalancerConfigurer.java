package com.github.mkopylec.charon.interceptors.rewrite;

import com.github.mkopylec.charon.configuration.Configurer;

public abstract class LoadBalancerConfigurer<B extends LoadBalancer> extends Configurer<B> {

    protected LoadBalancerConfigurer(B configuredObject) {
        super(configuredObject);
    }

    @Override
    protected B configure() {
        return super.configure();
    }
}
