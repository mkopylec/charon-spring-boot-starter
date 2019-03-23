package com.github.mkopylec.charon.configuration;

import com.github.mkopylec.charon.core.interceptors.RandomLoadBalancer;

public class RandomLoadBalancerConfigurer extends LoadBalancerConfigurer<RandomLoadBalancer> {

    protected RandomLoadBalancerConfigurer() {
        super(new RandomLoadBalancer());
    }

    public static RandomLoadBalancerConfigurer randomLoadBalancer() {
        return new RandomLoadBalancerConfigurer();
    }
}
