package com.github.mkopylec.charon.forwarding;

import com.github.mkopylec.charon.configuration.Valid;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType;

public interface BasicRequestForwardingInterceptor extends Valid, Comparable<BasicRequestForwardingInterceptor> {

    RequestForwardingInterceptorType getType();

    @Override
    default int compareTo(BasicRequestForwardingInterceptor requestForwardingInterceptor) {
        return getType().compareTo(requestForwardingInterceptor.getType());
    }
}
