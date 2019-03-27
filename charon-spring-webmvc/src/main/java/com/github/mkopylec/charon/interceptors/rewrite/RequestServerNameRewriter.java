package com.github.mkopylec.charon.interceptors.rewrite;

import java.net.URI;
import java.util.List;

import com.github.mkopylec.charon.forwarding.HttpRequest;
import com.github.mkopylec.charon.forwarding.HttpResponse;
import com.github.mkopylec.charon.forwarding.RequestForwarder;
import com.github.mkopylec.charon.forwarding.RequestForwarding;
import com.github.mkopylec.charon.interceptors.RequestForwardingInterceptor;

import static com.github.mkopylec.charon.interceptors.rewrite.RandomLoadBalancerConfigurer.randomLoadBalancer;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.springframework.util.Assert.isTrue;

class RequestServerNameRewriter implements RequestForwardingInterceptor {

    private LoadBalancer loadBalancer;
    private List<URI> outgoingServers;

    RequestServerNameRewriter() {
        loadBalancer = randomLoadBalancer().configure();
    }

    @Override
    public HttpResponse forward(HttpRequest request, RequestForwarding forwarding, RequestForwarder forwarder) {
        // TODO Implement server name rewrite
        return forwarder.forward(request, forwarding);
    }

    @Override
    public void validate() {
        loadBalancer.validate();
        isTrue(isNotEmpty(outgoingServers), "No outgoing servers set");
    }

    @Override
    public int getOrder() {
        return REQUEST_SERVER_NAME_REWRITER_ORDER;
    }

    void setLoadBalancer(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    void setOutgoingServers(List<String> outgoingServers) {
        this.outgoingServers = outgoingServers.stream()
                .map(URI::create)
                .collect(toList());
    }
}
