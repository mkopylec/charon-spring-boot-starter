package com.github.mkopylec.charon.forwarding.interceptors;

import com.github.mkopylec.charon.configuration.Valid;
import reactor.core.publisher.Mono;

import org.springframework.core.Ordered;

public interface RequestForwardingInterceptor extends Ordered, Valid {

    Mono<HttpResponse> forward(HttpRequest request, HttpRequestExecution execution);
}
