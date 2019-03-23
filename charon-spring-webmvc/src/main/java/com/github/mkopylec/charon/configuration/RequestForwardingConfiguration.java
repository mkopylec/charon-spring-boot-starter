package com.github.mkopylec.charon.configuration;

import java.util.ArrayList;
import java.util.List;

import com.github.mkopylec.charon.core.interceptors.RequestForwardingInterceptor;

import org.springframework.core.Ordered;

import static java.util.Collections.unmodifiableList;
import static java.util.Comparator.comparingInt;
import static org.springframework.util.Assert.hasText;

public class RequestForwardingConfiguration {

    private String name;
    private String path;
    private TimeoutConfiguration timeoutConfiguration;
    private List<RequestForwardingInterceptor> requestForwardingInterceptors;
    private CustomConfiguration customConfiguration;

    RequestForwardingConfiguration(String name) {
        this.name = name;
        path = "/";
        requestForwardingInterceptors = new ArrayList<>();
    }

    void validate() {
        hasText(name, "No request forwarding name set");
        timeoutConfiguration.validate();
        requestForwardingInterceptors.forEach(RequestForwardingInterceptor::validate);
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    void setPath(String path) {
        this.path = path;
    }

    public TimeoutConfiguration getTimeoutConfiguration() {
        return timeoutConfiguration;
    }

    void setTimeoutConfiguration(TimeoutConfiguration timeoutConfiguration) {
        this.timeoutConfiguration = timeoutConfiguration;
    }

    void mergeTimeoutConfiguration(TimeoutConfiguration timeoutConfiguration) {
        if (this.timeoutConfiguration == null) {
            this.timeoutConfiguration = timeoutConfiguration;
        }
    }

    public List<RequestForwardingInterceptor> getRequestForwardingInterceptors() {
        return unmodifiableList(requestForwardingInterceptors);
    }

    void addRequestForwardingInterceptor(RequestForwardingInterceptor requestForwardingInterceptor) {
        requestForwardingInterceptors.removeIf(interceptor -> interceptor.getOrder() == requestForwardingInterceptor.getOrder());
        requestForwardingInterceptors.add(requestForwardingInterceptor);
        requestForwardingInterceptors.sort(comparingInt(Ordered::getOrder));
    }

    void mergeRequestForwardingInterceptors(List<RequestForwardingInterceptor> requestForwardingInterceptors) {
        // TODO Merge, mind the enabled flag and joining lists
    }

    public CustomConfiguration getCustomConfiguration() {
        return customConfiguration;
    }

    void setCustomConfiguration(CustomConfiguration customConfiguration) {
        this.customConfiguration = customConfiguration;
    }
}
