package com.github.mkopylec.charon.test;

import com.github.mkopylec.charon.forwarding.interceptors.rewrite.LoadBalancerConfigurer;

public class LowestPortLoadBalancerConfigurer extends LoadBalancerConfigurer<LowestPortLoadBalancer> {

    private LowestPortLoadBalancerConfigurer() {
        super(new LowestPortLoadBalancer());
    }

    public static LowestPortLoadBalancerConfigurer lowestPortLoadBalancer() {
        return new LowestPortLoadBalancerConfigurer();
    }
}
