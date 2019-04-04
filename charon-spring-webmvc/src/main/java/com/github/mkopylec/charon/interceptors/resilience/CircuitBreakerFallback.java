package com.github.mkopylec.charon.interceptors.resilience;

import com.github.mkopylec.charon.interceptors.HttpResponse;

public interface CircuitBreakerFallback {

    HttpResponse run(Exception e);
}
