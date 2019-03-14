package com.github.mkopylec.charon;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.micrometer.RateLimiterMetrics;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;

public class Main {

    public static void main(String[] args) {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom().build();
        RetryConfig retryConfig = RetryConfig.custom().build();
        RateLimiterConfig rateLimiterConfig = RateLimiterConfig.custom().build();

        CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("c1");
        Retry retry = Retry.ofDefaults("r1");
        Runnable runnable = CircuitBreaker.decorateRunnable(circuitBreaker, () -> {
            System.out.println("dupa");
            throw new RuntimeException();
        });
        Runnable runnable1 = Retry.decorateRunnable(retry, runnable);
        runnable1.run();
        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);
        circuitBreakerRegistry.circuitBreaker("d");
    }
}
