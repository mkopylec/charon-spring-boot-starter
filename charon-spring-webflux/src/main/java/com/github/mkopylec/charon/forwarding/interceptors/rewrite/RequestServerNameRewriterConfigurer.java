package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorConfigurer;

import java.util.List;

import static java.util.Arrays.asList;

public class RequestServerNameRewriterConfigurer extends RequestForwardingInterceptorConfigurer<RequestServerNameRewriter> {

    private RequestServerNameRewriterConfigurer() {
        super(new RequestServerNameRewriter());
    }

    public static RequestServerNameRewriterConfigurer requestServerNameRewriter() {
        return new RequestServerNameRewriterConfigurer();
    }

    public RequestServerNameRewriterConfigurer loadBalancer(LoadBalancerConfigurer<?> loadBalancerConfigurer) {
        configuredObject.setLoadBalancer(loadBalancerConfigurer.configure());
        return this;
    }

    public RequestServerNameRewriterConfigurer outgoingServers(String... outgoingServers) {
        configuredObject.setOutgoingServers(asList(outgoingServers));
        return this;
    }

    public RequestServerNameRewriterConfigurer outgoingServers(List<String> outgoingServers) {
        configuredObject.setOutgoingServers(outgoingServers);
        return this;
    }
}
