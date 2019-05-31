package com.github.mkopylec.charon;

import java.net.URI;

import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.resilience.CircuitBreakerConfigurer;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import org.slf4j.Logger;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.ClientHttpResponse;

import static com.github.mkopylec.charon.configuration.CharonConfigurer.charonConfiguration;
import static com.github.mkopylec.charon.configuration.RequestMappingConfigurer.requestMapping;
import static com.github.mkopylec.charon.forwarding.CustomConfigurer.custom;
import static com.github.mkopylec.charon.forwarding.RestTemplateConfigurer.restTemplate;
import static com.github.mkopylec.charon.forwarding.TimeoutConfigurer.timeout;
import static com.github.mkopylec.charon.forwarding.interceptors.async.AsynchronousForwarderConfigurer.asynchronousForwarder;
import static com.github.mkopylec.charon.forwarding.interceptors.async.ThreadPoolConfigurer.threadPool;
import static com.github.mkopylec.charon.forwarding.interceptors.resilience.RetryerConfigurer.retryer;
import static com.github.mkopylec.charon.forwarding.interceptors.rewrite.RegexRequestPathRewriterConfigurer.regexRequestPathRewriter;
import static com.github.mkopylec.charon.forwarding.interceptors.rewrite.RemovingResponseCookiesRewriterConfigurer.removingResponseCookiesRewriter;
import static java.time.Duration.ofMillis;
import static org.slf4j.LoggerFactory.getLogger;

public class Main {

    private static final Logger log = getLogger(Main.class);

    public static void main(String[] args) {
        URI uri = URI.create("service://localhost/test");
        System.out.println(uri.getAuthority());
        System.out.println(uri.getUserInfo());
        System.out.println(uri.getSchemeSpecificPart());
        System.out.println(uri.getScheme());
        System.out.println(uri.getPath());
        System.out.println(uri.getPort());
        System.out.println(uri);
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom().ringBufferSizeInClosedState(2).build();
        RetryConfig retryConfig = RetryConfig.custom().build();
        RateLimiterConfig rateLimiterConfig = RateLimiterConfig.custom().build();

        CircuitBreaker circuitBreaker = CircuitBreaker.of("c1", circuitBreakerConfig);
        circuitBreaker.onError(1, null);
        Retry retry = Retry.ofDefaults("r1");
        Runnable runnable = CircuitBreaker.decorateRunnable(circuitBreaker, () -> {
            System.out.println("dupa");
            throw new RuntimeException();
        });
        try {
            runnable.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            runnable.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Runnable runnable1 = Retry.decorateRunnable(retry, runnable);
        runnable1.run();
        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);
        circuitBreakerRegistry.circuitBreaker("d");

        CircuitBreakerConfigurer.circuitBreaker();
        charonConfiguration()
                .set(restTemplate()
                        .set(timeout().connection(ofMillis(100)).read(ofMillis(500))))
                .set(removingResponseCookiesRewriter())
                .set(regexRequestPathRewriter())
                .set(retryer().configuration(RetryConfig.<HttpResponse>custom().retryOnResult(null)))
                .set(asynchronousForwarder()
                        .set(threadPool().coreSize(3)))
                .add(requestMapping("proxy 1")
                        .set(custom().set("name", "value")))
                .add(requestMapping("proxy 2"));

//        Retry.decorateSupplier(retry, () -> {
//            throw new IllegalArgumentException("dupa");
//        }).get();

//        try {
//            CircuitBreaker.decorateRunnable(circuitBreaker, () -> {
//                throw new IllegalArgumentException("dupa");
//            }).run();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        CircuitBreaker.decorateRunnable(circuitBreaker, () -> {
//            throw new IllegalArgumentException("dupa");
//        }).run();

        new RestTemplateBuilder().additionalInterceptors(
                (request, body, execution) -> {
                    System.out.println("start 1");
                    ClientHttpResponse execute = execution.execute(request, body);
                    System.out.println("end 1");
                    return execute;
                }, (request, body, execution) -> {
                    System.out.println("start 2");
                    ClientHttpResponse execute = execution.execute(request, body);
                    System.out.println("end 2");
                    return execute;
                }, (request, body, execution) -> {
                    System.out.println("start 3");
                    ClientHttpResponse execute = execution.execute(request, body);
                    System.out.println("end 3");
                    return execute;
                }).build().getForEntity("http://wykop.pl", String.class);

//        List<RequestForwardingInterceptor> interceptors = new ArrayList<>();
//        interceptors.add((request, forwarder) -> {
//            System.out.println("start 1");
//            HttpResponse forward = forwarder.forward(request);
//            System.out.println("end 1");
//            return null;
//        });
//        interceptors.add((request, forwarder) -> {
//            System.out.println("start 2");
//            HttpResponse forward = CircuitBreaker.decorateSupplier(circuitBreaker, () -> forwarder.forward(request)).get();
//            System.out.println("end 2");
//            return forward;
//        });
//        interceptors.add((request, forwarder) -> {
//            System.out.println("start 3");
//            HttpResponse forward = Retry.decorateSupplier(retry, () -> forwarder.forward(request)).get();
//            System.out.println("end 3");
//            return forward;
//        });
//        for (int i = 0; i < 10; i++) {
//            RequestForwarder forwarder = new RequestForwarder(interceptors);
//            try {
//                forwarder.forward(null);
//            } catch (Exception e) {
//                log.error("error", e);
//                log.info("CB state {}", circuitBreaker.getState());
//            }
//        }
    }
}
