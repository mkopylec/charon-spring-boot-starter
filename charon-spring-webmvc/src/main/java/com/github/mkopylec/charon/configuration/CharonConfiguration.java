package com.github.mkopylec.charon.configuration;

import java.util.ArrayList;
import java.util.List;

import com.github.mkopylec.charon.forwarding.HttpClientFactory;
import com.github.mkopylec.charon.interceptors.RequestForwardingInterceptor;
import com.github.mkopylec.charon.utils.Valid;

import org.springframework.core.Ordered;

import static com.github.mkopylec.charon.configuration.CustomConfigurer.custom;
import static com.github.mkopylec.charon.configuration.RequestForwardingConfigurer.requestForwarding;
import static com.github.mkopylec.charon.configuration.TimeoutConfigurer.timeout;
import static com.github.mkopylec.charon.interceptors.rewrite.CopyRequestPathRewriterConfigurer.copyRequestPathRewriter;
import static com.github.mkopylec.charon.interceptors.rewrite.RootPathResponseCookieRewriterConfigurer.rootPathResponseCookieRewriter;
import static java.util.Collections.unmodifiableList;
import static java.util.Comparator.comparingInt;
import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

public class CharonConfiguration implements Valid {

    private int filterOrder;
    private TimeoutConfiguration timeoutConfiguration;
    private HttpClientFactory httpClientFactory;
    private List<RequestForwardingInterceptor> requestForwardingInterceptors;
    private List<RequestForwardingConfiguration> requestForwardingConfigurations;
    private CustomConfiguration customConfiguration;

    CharonConfiguration() {
        filterOrder = LOWEST_PRECEDENCE;
        timeoutConfiguration = timeout().configure();
        requestForwardingInterceptors = new ArrayList<>();
        addRequestForwardingInterceptor(copyRequestPathRewriter().configure());
        addRequestForwardingInterceptor(rootPathResponseCookieRewriter().configure());
        requestForwardingConfigurations = new ArrayList<>();
        addRequestForwardingConfiguration(requestForwarding("default").configure());
        customConfiguration = custom().configure();
    }

    public int getFilterOrder() {
        return filterOrder;
    }

    void setFilterOrder(int filterOrder) {
        this.filterOrder = filterOrder;
    }

    void setTimeoutConfiguration(TimeoutConfiguration timeoutConfiguration) {
        this.timeoutConfiguration = timeoutConfiguration;
    }

    public HttpClientFactory getHttpClientFactory() {
        return httpClientFactory;
    }

    void setHttpClientFactory(HttpClientFactory httpClientFactory) {
        this.httpClientFactory = httpClientFactory;
    }

    void addRequestForwardingInterceptor(RequestForwardingInterceptor requestForwardingInterceptor) {
        requestForwardingInterceptors.removeIf(interceptor -> interceptor.getOrder() == requestForwardingInterceptor.getOrder());
        requestForwardingInterceptors.add(requestForwardingInterceptor);
        requestForwardingInterceptors.sort(comparingInt(Ordered::getOrder));
    }

    public List<RequestForwardingConfiguration> getRequestForwardingConfigurations() {
        return unmodifiableList(requestForwardingConfigurations);
    }

    void addRequestForwardingConfiguration(RequestForwardingConfiguration requestForwardingConfiguration) {
        requestForwardingConfigurations.add(requestForwardingConfiguration);
    }

    void setCustomConfiguration(CustomConfiguration customConfiguration) {
        this.customConfiguration = customConfiguration;
    }

    private void mergeWithRequestForwardingConfigurations() {
        requestForwardingConfigurations.forEach(configuration -> {
            configuration.mergeTimeoutConfiguration(timeoutConfiguration);
            configuration.mergeRequestForwardingInterceptors(requestForwardingInterceptors);
            configuration.mergeCustomConfiguration(customConfiguration);
        });
    }
}
