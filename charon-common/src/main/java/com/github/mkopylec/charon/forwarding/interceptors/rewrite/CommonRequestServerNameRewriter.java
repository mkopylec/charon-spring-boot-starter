package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import java.net.URI;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import com.github.mkopylec.charon.configuration.Valid;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType;
import org.slf4j.Logger;

import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.REQUEST_SERVER_NAME_REWRITER;
import static com.github.mkopylec.charon.forwarding.interceptors.rewrite.RandomLoadBalancerConfigurer.randomLoadBalancer;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.springframework.util.Assert.isTrue;
import static org.springframework.web.util.UriComponentsBuilder.fromUri;

abstract class CommonRequestServerNameRewriter implements Valid {

    private static final Pattern outgoingServerPattern = Pattern.compile("\\w+://.+");

    private Logger log;
    private LoadBalancer loadBalancer;
    private List<URI> outgoingServers;

    CommonRequestServerNameRewriter(Logger log) {
        this.log = log;
        loadBalancer = randomLoadBalancer().configure();
    }

    @Override
    public void validate() {
        loadBalancer.validate();
        isTrue(isNotEmpty(outgoingServers), "No outgoing servers set");
    }

    public RequestForwardingInterceptorType getType() {
        return REQUEST_SERVER_NAME_REWRITER;
    }

    void setLoadBalancer(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    void setOutgoingServers(List<String> outgoingServers) {
        this.outgoingServers = outgoingServers.stream()
                .map(server -> outgoingServerPattern.matcher(server).matches() ? server : "http://" + server)
                .map(URI::create)
                .collect(toList());
    }

    void rewriteServerName(URI uri, Consumer<URI> rewrittenUriSetter) {
        String oldServerName = uri.getScheme() + "://" + uri.getAuthority();
        URI rewrittenServerName = loadBalancer.chooseServer(outgoingServers);
        URI rewrittenUri = fromUri(uri)
                .scheme(rewrittenServerName.getScheme())
                .host(rewrittenServerName.getHost())
                .port(rewrittenServerName.getPort())
                .build(true)
                .toUri();
        rewrittenUriSetter.accept(rewrittenUri);
        log.debug("Request server name rewritten from {} to {}", oldServerName, rewrittenServerName);
    }

    void logStart(String mappingName) {
        log.trace("[Start] Request server name rewriting for '{}' request mapping", mappingName);
    }

    void logEnd(String mappingName) {
        log.trace("[End] Request server name rewriting for '{}' request mapping", mappingName);
    }
}
