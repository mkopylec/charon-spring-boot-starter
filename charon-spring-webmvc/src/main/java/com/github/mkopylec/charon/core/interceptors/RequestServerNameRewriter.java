package com.github.mkopylec.charon.core.interceptors;

import java.net.URI;
import java.util.List;

import com.github.mkopylec.charon.core.HttpRequest;
import com.github.mkopylec.charon.core.HttpResponse;
import com.github.mkopylec.charon.core.RequestForwarder;
import com.github.mkopylec.charon.core.RequestForwarding;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.springframework.util.Assert.isTrue;

public class RequestServerNameRewriter implements RequestForwardingInterceptor {

    private LoadBalancer loadBalancer;
    private List<URI> outgoingServers;

    public RequestServerNameRewriter() {
        loadBalancer = new RandomLoadBalancer();
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

    public LoadBalancer getLoadBalancer() {
        return loadBalancer;
    }

    public void setLoadBalancer(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    public List<URI> getOutgoingServers() {
        return outgoingServers;
    }

    public void setOutgoingServers(List<String> outgoingServers) {
        this.outgoingServers = outgoingServers.stream()
                .map(URI::create)
                .collect(toList());
    }
}
