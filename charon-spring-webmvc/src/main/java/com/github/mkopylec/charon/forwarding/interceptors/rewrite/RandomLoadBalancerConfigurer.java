package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

public class RandomLoadBalancerConfigurer extends LoadBalancerConfigurer<RandomLoadBalancer> {

    private RandomLoadBalancerConfigurer() {
        super(new RandomLoadBalancer());
    }

    public static RandomLoadBalancerConfigurer randomLoadBalancer() {
        return new RandomLoadBalancerConfigurer();
    }
}
