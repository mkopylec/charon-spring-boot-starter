package com.github.mkopylec.charon.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.github.mkopylec.charon.forwarding.WebClientConfiguration;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType;

import static com.github.mkopylec.charon.forwarding.interceptors.rewrite.RequestProtocolHeadersRewriterConfigurer.requestProtocolHeadersRewriter;
import static com.github.mkopylec.charon.forwarding.interceptors.rewrite.RequestProxyHeadersRewriterConfigurer.requestProxyHeadersRewriter;
import static com.github.mkopylec.charon.forwarding.interceptors.rewrite.ResponseProtocolHeadersRewriterConfigurer.responseProtocolHeadersRewriter;
import static com.github.mkopylec.charon.forwarding.interceptors.rewrite.RootPathResponseCookiesRewriterConfigurer.rootPathResponseCookiesRewriter;
import static java.util.Collections.sort;
import static java.util.Collections.unmodifiableList;
import static java.util.regex.Pattern.compile;
import static org.springframework.util.Assert.hasText;

public class RequestMappingConfiguration implements Valid {

    private String name;
    private Pattern pathRegex;
    private WebClientConfiguration webClientConfiguration;
    private List<RequestForwardingInterceptor> requestForwardingInterceptors;
    private List<RequestForwardingInterceptorType> unsetRequestForwardingInterceptors;

    RequestMappingConfiguration(String name) {
        this.name = name;
        pathRegex = compile("/.*");
        requestForwardingInterceptors = new ArrayList<>();
        addRequestForwardingInterceptor(requestProtocolHeadersRewriter().configure());
        addRequestForwardingInterceptor(requestProxyHeadersRewriter().configure());
        addRequestForwardingInterceptor(responseProtocolHeadersRewriter().configure());
        addRequestForwardingInterceptor(rootPathResponseCookiesRewriter().configure());
        unsetRequestForwardingInterceptors = new ArrayList<>();
    }

    @Override
    public void validate() {
        hasText(name, "No request forwarding name set");
    }

    public String getName() {
        return name;
    }

    public Pattern getPathRegex() {
        return pathRegex;
    }

    void setPathRegex(String pathRegex) {
        this.pathRegex = compile(pathRegex);
    }

    public WebClientConfiguration getWebClientConfiguration() {
        return webClientConfiguration;
    }

    void setWebClientConfiguration(WebClientConfiguration webClientConfiguration) {
        this.webClientConfiguration = webClientConfiguration;
    }

    void mergeRestTemplateConfiguration(WebClientConfiguration webClientConfiguration) {
        if (this.webClientConfiguration == null) {
            this.webClientConfiguration = webClientConfiguration;
        }
    }

    public List<RequestForwardingInterceptor> getRequestForwardingInterceptors() {
        return unmodifiableList(requestForwardingInterceptors);
    }

    void addRequestForwardingInterceptor(RequestForwardingInterceptor requestForwardingInterceptor) {
        removeRequestForwardingInterceptor(requestForwardingInterceptors, requestForwardingInterceptor.getType());
        requestForwardingInterceptors.add(requestForwardingInterceptor);
    }

    void removeRequestForwardingInterceptor(RequestForwardingInterceptorType requestForwardingInterceptorType) {
        unsetRequestForwardingInterceptors.add(requestForwardingInterceptorType);
    }

    void mergeRequestForwardingInterceptors(List<RequestForwardingInterceptor> requestForwardingInterceptors) {
        List<RequestForwardingInterceptor> globalInterceptors = new ArrayList<>(requestForwardingInterceptors);
        this.requestForwardingInterceptors.forEach(interceptor -> removeRequestForwardingInterceptor(globalInterceptors, interceptor.getType()));
        this.requestForwardingInterceptors.addAll(globalInterceptors);
        sort(this.requestForwardingInterceptors);
        unsetRequestForwardingInterceptors.forEach(interceptorType -> removeRequestForwardingInterceptor(this.requestForwardingInterceptors, interceptorType));
    }

    @Override
    public String toString() {
        return "'" + name + "'";
    }

    private void removeRequestForwardingInterceptor(List<RequestForwardingInterceptor> requestForwardingInterceptors, RequestForwardingInterceptorType requestForwardingInterceptorType) {
        requestForwardingInterceptors.removeIf(interceptor -> interceptor.getType().equals(requestForwardingInterceptorType));
    }
}
