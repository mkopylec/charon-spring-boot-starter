package com.github.mkopylec.charon.configuration;

import java.util.function.Consumer;

import com.github.mkopylec.charon.core.interceptors.LoadBalancer;

public abstract class LoadBalancerConfigurer<B extends LoadBalancer> {

    private B loadBalancer;

    protected LoadBalancerConfigurer(B loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    protected void configure(Consumer<B> balancerConfigurer) {
        balancerConfigurer.accept(loadBalancer);
    }

    B getLoadBalancer() {
        return loadBalancer;
    }
}
