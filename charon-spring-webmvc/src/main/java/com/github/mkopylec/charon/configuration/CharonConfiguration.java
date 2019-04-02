package com.github.mkopylec.charon.configuration;

import java.util.ArrayList;
import java.util.List;

import com.github.mkopylec.charon.forwarding.CustomConfiguration;
import com.github.mkopylec.charon.forwarding.RestTemplateConfiguration;
import com.github.mkopylec.charon.forwarding.TimeoutConfiguration;
import com.github.mkopylec.charon.interceptors.RequestForwardingInterceptor;

import static com.github.mkopylec.charon.configuration.RequestForwardingConfigurer.requestForwarding;
import static com.github.mkopylec.charon.forwarding.CustomConfigurer.custom;
import static com.github.mkopylec.charon.forwarding.OkHttpRestTemplateConfigurer.okHttpRestTemplate;
import static com.github.mkopylec.charon.forwarding.TimeoutConfigurer.timeout;
import static com.github.mkopylec.charon.interceptors.rewrite.CopyRequestPathRewriterConfigurer.copyRequestPathRewriter;
import static com.github.mkopylec.charon.interceptors.rewrite.RootPathResponseCookieRewriterConfigurer.rootPathResponseCookieRewriter;
import static java.util.Collections.unmodifiableList;
import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

public class CharonConfiguration implements Valid {

    private int filterOrder;
    private TimeoutConfiguration timeoutConfiguration;
    private RestTemplateConfiguration restTemplateConfiguration;
    private List<RequestForwardingInterceptor> requestForwardingInterceptors;
    private List<RequestForwardingConfiguration> requestForwardingConfigurations;
    private CustomConfiguration customConfiguration;

    CharonConfiguration() {
        filterOrder = LOWEST_PRECEDENCE;
        timeoutConfiguration = timeout().configure();
        restTemplateConfiguration = okHttpRestTemplate().configure();
        requestForwardingInterceptors = new ArrayList<>();
        addRequestForwardingInterceptor(copyRequestPathRewriter().configure());
        addRequestForwardingInterceptor(rootPathResponseCookieRewriter().configure());
        // TODO Think about more default interceptors
        requestForwardingConfigurations = new ArrayList<>();
        addRequestForwardingConfiguration(requestForwarding("default").configure());
        customConfiguration = custom().configure();
    }

    int getFilterOrder() {
        return filterOrder;
    }

    void setFilterOrder(int filterOrder) {
        this.filterOrder = filterOrder;
    }

    void setTimeoutConfiguration(TimeoutConfiguration timeoutConfiguration) {
        this.timeoutConfiguration = timeoutConfiguration;
    }

    RestTemplateConfiguration getRestTemplateConfiguration() {
        return restTemplateConfiguration;
    }

    void setRestTemplateConfiguration(RestTemplateConfiguration restTemplateConfiguration) {
        this.restTemplateConfiguration = restTemplateConfiguration;
    }

    void addRequestForwardingInterceptor(RequestForwardingInterceptor requestForwardingInterceptor) {
        requestForwardingInterceptors.removeIf(interceptor -> interceptor.getOrder() == requestForwardingInterceptor.getOrder());
        requestForwardingInterceptors.add(requestForwardingInterceptor);
    }

    List<RequestForwardingConfiguration> getRequestForwardingConfigurations() {
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
