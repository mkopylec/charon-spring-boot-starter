package com.github.mkopylec.charon.configuration;

import java.util.List;

import com.github.mkopylec.charon.core.interceptors.RequestServerNameRewriter;

import static java.util.Arrays.asList;

public class RequestServerNameRewriterConfigurer extends RequestForwardingInterceptorConfigurer<RequestServerNameRewriter> {

    protected RequestServerNameRewriterConfigurer() {
        super(new RequestServerNameRewriter());
    }

    public static RequestServerNameRewriterConfigurer requestServerNameRewriter() {
        return new RequestServerNameRewriterConfigurer();
    }

    public RequestServerNameRewriterConfigurer loadBalancer(LoadBalancerConfigurer<?> loadBalancerConfigurer) {
        configure(requestServerNameRewriter -> requestServerNameRewriter.setLoadBalancer(loadBalancerConfigurer.getLoadBalancer()));
        return this;
    }

    public RequestServerNameRewriterConfigurer outgoingServers(String... outgoingServers) {
        configure(requestServerNameRewriter -> requestServerNameRewriter.setOutgoingServers(asList(outgoingServers)));
        return this;
    }

    public RequestServerNameRewriterConfigurer outgoingServers(List<String> outgoingServers) {
        configure(requestServerNameRewriter -> requestServerNameRewriter.setOutgoingServers(outgoingServers));
        return this;
    }
}
