package com.github.mkopylec.charon.configuration;

import java.util.ArrayList;
import java.util.List;

import com.github.mkopylec.charon.core.interceptors.RequestForwardingInterceptor;

import org.springframework.core.Ordered;

import static com.github.mkopylec.charon.configuration.CopyRequestPathRewriterConfigurer.copyRequestPathRewriter;
import static com.github.mkopylec.charon.configuration.RootPathResponseCookieRewriterConfigurer.rootPathResponseCookieRewriter;
import static com.github.mkopylec.charon.configuration.TimeoutConfigurer.timeout;
import static java.util.Collections.unmodifiableList;
import static java.util.Comparator.comparingInt;
import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

public class CharonConfiguration {

    private int filterOrder;
    private TimeoutConfiguration timeoutConfiguration;
    private List<RequestForwardingInterceptor> requestForwardingInterceptors;
    private List<RequestForwardingConfiguration> requestForwardingConfigurations;
    private CustomConfiguration customConfiguration;

    CharonConfiguration() {
        filterOrder = LOWEST_PRECEDENCE;
        timeoutConfiguration = timeout().getConfiguration();
        requestForwardingInterceptors = new ArrayList<>();
        requestForwardingInterceptors.add(copyRequestPathRewriter().getRequestForwardingInterceptor());
        requestForwardingInterceptors.add(rootPathResponseCookieRewriter().getRequestForwardingInterceptor());
        requestForwardingConfigurations = new ArrayList<>();
        requestForwardingConfigurations.add(new RequestForwardingConfiguration("default"));
    }

    void validate() {
        timeoutConfiguration.validate();
        requestForwardingInterceptors.forEach(RequestForwardingInterceptor::validate);
        requestForwardingConfigurations.forEach(RequestForwardingConfiguration::validate);
    }

    public int getFilterOrder() {
        return filterOrder;
    }

    void setFilterOrder(int filterOrder) {
        this.filterOrder = filterOrder;
    }

    public TimeoutConfiguration getTimeoutConfiguration() {
        return timeoutConfiguration;
    }

    void setTimeoutConfiguration(TimeoutConfiguration timeoutConfiguration) {
        this.timeoutConfiguration = timeoutConfiguration;
    }

    public List<RequestForwardingInterceptor> getRequestForwardingInterceptors() {
        return unmodifiableList(requestForwardingInterceptors);
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

    public CustomConfiguration getCustomConfiguration() {
        return customConfiguration;
    }

    void setCustomConfiguration(CustomConfiguration customConfiguration) {
        this.customConfiguration = customConfiguration;
    }
}
