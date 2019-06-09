package com.github.mkopylec.charon.forwarding.interceptors;

import com.github.mkopylec.charon.configuration.Valid;

import org.springframework.core.Ordered;

public interface RequestForwardingInterceptor extends Ordered, Valid {

    HttpResponse forward(HttpRequest request, HttpRequestExecution execution);
}
