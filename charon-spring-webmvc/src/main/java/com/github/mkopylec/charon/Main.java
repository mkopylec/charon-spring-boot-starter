package com.github.mkopylec.charon;

import java.net.URI;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;

import static com.github.mkopylec.charon.configuration.AsynchronousForwardingConfigurer.asynchronousForwarding;
import static com.github.mkopylec.charon.configuration.CharonConfigurer.charon;
import static com.github.mkopylec.charon.configuration.RateLimiterConfigurer.rateLimiter;
import static com.github.mkopylec.charon.configuration.RequestForwardingConfigurer.requestForwarding;
import static com.github.mkopylec.charon.configuration.ThreadPoolConfigurer.threadPool;
import static com.github.mkopylec.charon.core.RegexRequestPathRewriterConfigurer.regexRequestPathRewriter;

public class Main {

    public static void main(String[] args) {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom().build();
        RetryConfig retryConfig = RetryConfig.custom().build();
        RateLimiterConfig rateLimiterConfig = RateLimiterConfig.custom().build();

        CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("c1");
        Retry retry = Retry.ofDefaults("r1");
        Runnable runnable = CircuitBreaker.decorateRunnable(circuitBreaker, () -> {
            System.out.println("dupa");
//            throw new RuntimeException();
        });
        Runnable runnable1 = Retry.decorateRunnable(retry, runnable);
        runnable1.run();
        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);
        circuitBreakerRegistry.circuitBreaker("d");

        charon()
                .configure(regexRequestPathRewriter())
                .configure(rateLimiter()
                        .configure(RateLimiterConfig.custom().limitForPeriod(1))
                        .measured(true))
                .configure(asynchronousForwarding()
                        .configure(threadPool().maximumSize(12).initialSize(1)))
                .add(requestForwarding("proxy 1")
                        .outgoingServers("localhost:8080", "localhost:8081")
                        .rateLimited(true)
                        .configure(rateLimiter()
                                .measured(false)));

        System.out.println(URI.create("localhost"));
        System.out.println(URI.create("localhost:8080"));
        System.out.println(URI.create("http://localhost:8080"));
        System.out.println(URI.create("https://localhost:8080"));
        System.out.println(URI.create("dupa://localhost"));
    }
}
