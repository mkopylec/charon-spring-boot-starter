package com.github.mkopylec.charon.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.github.mkopylec.charon.interceptors.RequestForwardingInterceptor;
import com.github.mkopylec.charon.utils.Valid;

import org.springframework.core.Ordered;

import static java.util.Collections.unmodifiableList;
import static java.util.Comparator.comparingInt;
import static java.util.regex.Pattern.compile;
import static org.springframework.util.Assert.hasText;

public class RequestForwardingConfiguration implements Valid {

    private String name;
    private Pattern pathRegex;
    private TimeoutConfiguration timeoutConfiguration;
    private List<RequestForwardingInterceptor> requestForwardingInterceptors;
    private CustomConfiguration customConfiguration;

    RequestForwardingConfiguration(String name) {
        this.name = name;
        pathRegex = compile("/.*");
        requestForwardingInterceptors = new ArrayList<>();
    }

    @Override
    public void validate() {
        hasText(name, "No request forwarding name set");
    }

    public String getName() {
        return name;
    }

    public String getLoggedName() {
        return "'" + name + "'";
    }

    public Pattern getPathRegex() {
        return pathRegex;
    }

    void setPathRegex(String pathRegex) {
        this.pathRegex = compile(pathRegex);
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
        removeRequestForwardingInterceptor(requestForwardingInterceptors, requestForwardingInterceptor.getOrder());
        requestForwardingInterceptors.add(requestForwardingInterceptor);
        requestForwardingInterceptors.sort(comparingInt(Ordered::getOrder));
    }

    void mergeRequestForwardingInterceptors(List<RequestForwardingInterceptor> requestForwardingInterceptors) {
        List<RequestForwardingInterceptor> globalInterceptors = new ArrayList<>(requestForwardingInterceptors);
        this.requestForwardingInterceptors.forEach(interceptor -> removeRequestForwardingInterceptor(globalInterceptors, interceptor.getOrder()));
        this.requestForwardingInterceptors.addAll(requestForwardingInterceptors);
    }

    public CustomConfiguration getCustomConfiguration() {
        return customConfiguration;
    }

    void setCustomConfiguration(CustomConfiguration customConfiguration) {
        this.customConfiguration = customConfiguration;
    }

    void mergeCustomConfiguration(CustomConfiguration customConfiguration) {
        if (this.customConfiguration == null) {
            this.customConfiguration = customConfiguration;
        }
    }

    private void removeRequestForwardingInterceptor(List<RequestForwardingInterceptor> requestForwardingInterceptors, int order) {
        requestForwardingInterceptors.removeIf(interceptor -> interceptor.getOrder() == order);
    }
}
