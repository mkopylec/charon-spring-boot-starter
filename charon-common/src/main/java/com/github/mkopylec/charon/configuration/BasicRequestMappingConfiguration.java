package com.github.mkopylec.charon.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.github.mkopylec.charon.forwarding.BasicRequestForwardingInterceptor;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import static java.util.Collections.sort;
import static java.util.Collections.unmodifiableList;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.Assert.hasText;

public abstract class BasicRequestMappingConfiguration implements Valid {

    private String name;
    private Pattern pathRegex;
    private List<BasicRequestForwardingInterceptor> requestForwardingInterceptors;
    private List<RequestForwardingInterceptorType> unsetRequestForwardingInterceptors;
    private BasicRequestMappingConfigurer<?, ?> requestMappingConfigurer;

    BasicRequestMappingConfiguration(String name) {
        this.name = name;
        pathRegex = compile("/.*");
        requestForwardingInterceptors = new ArrayList<>();
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

    public List<BasicRequestForwardingInterceptor> getRequestForwardingInterceptors() {
        return unmodifiableList(requestForwardingInterceptors);
    }

    void addRequestForwardingInterceptor(BasicRequestForwardingInterceptor requestForwardingInterceptor) {
        removeRequestForwardingInterceptor(requestForwardingInterceptors, requestForwardingInterceptor.getType());
        unsetRequestForwardingInterceptors.remove(requestForwardingInterceptor.getType());
        requestForwardingInterceptors.add(requestForwardingInterceptor);
    }

    void removeRequestForwardingInterceptor(RequestForwardingInterceptorType requestForwardingInterceptorType) {
        removeRequestForwardingInterceptor(requestForwardingInterceptors, requestForwardingInterceptorType);
        unsetRequestForwardingInterceptors.add(requestForwardingInterceptorType);
    }

    void mergeRequestForwardingInterceptors(List<BasicRequestForwardingInterceptor> requestForwardingInterceptors) {
        List<BasicRequestForwardingInterceptor> additionalInterceptors = requestForwardingInterceptors.stream()
                .filter(interceptor -> this.requestForwardingInterceptors.stream().noneMatch(i -> i.getType().equals(interceptor.getType())))
                .filter(interceptor -> unsetRequestForwardingInterceptors.stream().noneMatch(i -> i.equals(interceptor.getType())))
                .collect(toList());
        this.requestForwardingInterceptors.addAll(additionalInterceptors);
        sort(this.requestForwardingInterceptors);
    }

    BasicRequestMappingConfigurer<?, ?> getRequestMappingConfigurer() {
        return requestMappingConfigurer;
    }

    void setRequestMappingConfigurer(BasicRequestMappingConfigurer<?, ?> requestMappingConfigurer) {
        this.requestMappingConfigurer = requestMappingConfigurer;
    }

    private void removeRequestForwardingInterceptor(List<BasicRequestForwardingInterceptor> requestForwardingInterceptors, RequestForwardingInterceptorType requestForwardingInterceptorType) {
        requestForwardingInterceptors.removeIf(interceptor -> interceptor.getType().equals(requestForwardingInterceptorType));
    }

    @Override
    public String toString() {
        return "'" + name + "'";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        BasicRequestMappingConfiguration rhs = (BasicRequestMappingConfiguration) obj;
        return new EqualsBuilder()
                .append(this.name, rhs.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(name)
                .toHashCode();
    }
}
