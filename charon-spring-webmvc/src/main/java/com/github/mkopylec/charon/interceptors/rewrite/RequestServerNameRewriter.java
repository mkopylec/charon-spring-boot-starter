package com.github.mkopylec.charon.interceptors.rewrite;

import java.net.URI;
import java.util.List;

import com.github.mkopylec.charon.interceptors.HttpRequest;
import com.github.mkopylec.charon.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.interceptors.HttpResponse;
import com.github.mkopylec.charon.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;

import static com.github.mkopylec.charon.interceptors.rewrite.RandomLoadBalancerConfigurer.randomLoadBalancer;
import static java.net.URI.create;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.util.Assert.isTrue;
import static org.springframework.web.util.UriComponentsBuilder.fromUri;

class RequestServerNameRewriter implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(RequestServerNameRewriter.class);

    private LoadBalancer loadBalancer;
    private List<URI> outgoingServers;

    RequestServerNameRewriter() {
        loadBalancer = randomLoadBalancer().configure();
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        log.trace("[Start] Request server name rewriting for '{}' request mapping", execution.getMappingName());
        rewriteServerName(request);
        HttpResponse response = execution.execute(request);
        log.trace("[End] Request server name rewriting for '{}' request mapping", execution.getMappingName());
        return response;
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
                .map(uri -> uri.getScheme() == null ? create("http://" + uri) : uri)
                .collect(toList());
    }

    private void rewriteServerName(HttpRequest request) {
        URI oldUri = request.getURI();
        String oldServerName = oldUri.getScheme() + "://" + oldUri.getAuthority();
        URI rewrittenServerName = loadBalancer.chooseServer(outgoingServers);
        URI rewrittenUri = fromUri(oldUri)
                .scheme(rewrittenServerName.getScheme())
                .host(rewrittenServerName.getHost())
                .port(rewrittenServerName.getPort())
                .build(true)
                .toUri();
        request.setUri(rewrittenUri);
        log.debug("Request server name rewritten from {} to {}", oldServerName, rewrittenServerName);
    }
}
