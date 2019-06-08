# Charon Spring Boot Starter
[![Build Status](https://travis-ci.org/mkopylec/charon-spring-boot-starter.svg?branch=master)](https://travis-ci.org/mkopylec/charon-spring-boot-starter)
[![Code Coverage](https://codecov.io/gh/mkopylec/charon-spring-boot-starter/branch/master/graph/badge.svg)](https://codecov.io/gh/mkopylec/charon-spring-boot-starter)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mkopylec/charon-spring-webmvc/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/com.github.mkopylec/charon-spring-webmvc)

Charon is a **reverse proxy** implementation.
It automatically forwards HTTP requests from one web application to another and sends back the received HTTP response to the client.
There are some alternative reverse proxy implementations like: [Zuul](https://github.com/Netflix/zuul/wiki) or [Smiley's HTTP Proxy Servlet](https://github.com/mitre/HTTP-Proxy-Servlet).
Zuul is highly bounded to [Spring Cloud Netflix](https://spring.io/projects/spring-cloud-netflix), Smiley's HTTP Proxy Servlet is a simple one, without advanced features.
Charon is a universal Spring Boot tool. It already has a lot features implemented and its architecture provides an easy way to add new ones.

## Features
- highly configurable and extensible
- Spring [WebMVC](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html) and [WebFlux](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html) support
- configurable request forwarding mappings
- custom load balancing
- flexible path rewriting
- [Resilience4j](https://resilience4j.github.io/resilience4j/) support
- metrics support based on [Micrometer](https://micrometer.io/)
- asynchronous request forwarding
- cookies rewriting
- 'X-Forwarded' HTTP headers support
- custom forwarding process intercepting
- configurable HTTP client

## Migrating from older versions to 4.x.x
Charon was completely rewritten, configuration via _application.yml_ file is no longer available.
Now Charon can be configured by in-code configuration.
See further documentation for more details. 

## Installing
Charon is published on [Maven Central](https://search.maven.org/search?q=mkopylec%20charon) repository.\
WebMVC module:
```gradle
dependencies {
    compile group: 'com.github.mkopylec', name: 'charon-spring-webmvc', version: '4.0.0'
}
```
WebFlux module:
```gradle
dependencies {
    compile group: 'com.github.mkopylec', name: 'charon-spring-webflux', version: '4.0.0'
}
```

## Basic usage
Create a Spring Boot web application:
```java
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```
Configure Charon by creating a `CharonConfigurer` Spring bean:
```java
import static com.github.mkopylec.charon.configuration.CharonConfigurer.charonConfiguration;
import static com.github.mkopylec.charon.configuration.RequestMappingConfigurer.requestMapping;
import static com.github.mkopylec.charon.forwarding.interceptors.rewrite.RequestServerNameRewriterConfigurer.requestServerNameRewriter;

@Configuration
class CharonConfiguration {

    @Bean
    CharonConfigurer charonConfigurer() {
        return charonConfiguration()
                .set(requestServerNameRewriter().outgoingServers("host1:8080", "host2:8081"))
                .add(requestMapping("all requests mapping"));
    }
}
```
When an application is configured like above then every incoming request will be forwarded to _http://host1:8080_ or _http://host2:8081_.

## Request mappings
Request mapping is a configuration that defines which and how incoming requests will be forwarded.
Multiple request mappings can be added.
The above configuration will use request mapping named `all requests mapping` to forward incoming requests.
Every request mapping must be named.
The request mapping which will handle the incoming request is chosen by a regular expression that defines incoming request paths.
If path regular expression is not set then the configured request mapping will handle all incoming requests.
Sample configuration of request mapping's path regular expression can look like this:
```java
import static com.github.mkopylec.charon.configuration.CharonConfigurer.charonConfiguration;
import static com.github.mkopylec.charon.configuration.RequestMappingConfigurer.requestMapping;

@Configuration
class CharonConfiguration {

    @Bean
    CharonConfigurer charonConfigurer() {
        return charonConfiguration()
                .add(requestMapping("mapping name")
                        .pathRegex("/.*"));
    }
}
```
More things can be configured for request mapping.
See the documentation below for more details.

## Configuration basics
Charon can be configured in many ways. 
This can be done via `CharonConfigurer` API.
Charon can be configured globally or per request mapping.
Request mapping configuration always overwrites the global one for a particular mapping.
For example if Charon is configured like below:
```java
import static com.github.mkopylec.charon.configuration.CharonConfigurer.charonConfiguration;
import static com.github.mkopylec.charon.configuration.RequestMappingConfigurer.requestMapping;
import static com.github.mkopylec.charon.forwarding.interceptors.rewrite.RequestServerNameRewriterConfigurer.requestServerNameRewriter;

@Configuration
class CharonConfiguration {

    @Bean
    CharonConfigurer charonConfigurer() {
        return charonConfiguration()
                .set(requestServerNameRewriter().outgoingServers("host1:8080"))
                .add(requestMapping("mapping 1"))
                .add(requestMapping("mapping 2")
                        .set(requestServerNameRewriter().outgoingServers("host2:8081")));
    }
}
```
then requests handled by `mapping 1` mapping wil be forwarded to _http://host1:8080_ but those handled by `mapping 2` mapping will be forwarded to _http://host2:8081_.

## Request forwarding interceptors
Most of the Charon's features are represented by request forwarding interceptors.
An interceptor allows to modify outgoing requests and incoming responses.
Every interceptor has an order that defines when the particular interceptor will be invoked.
Some of the interceptors are added to Charon configuration by default, some can be added manually.
Interceptors can be freely set and unset globally and per request mapping:
```java
import static com.github.mkopylec.charon.configuration.CharonConfigurer.charonConfiguration;
import static com.github.mkopylec.charon.configuration.RequestMappingConfigurer.requestMapping;

@Configuration
class CharonConfiguration {

    @Bean
    CharonConfigurer charonConfigurer() {
        return charonConfiguration()
                .set(<interceptor>)
                .unset(<interceptor order>)
                .add(requestMapping("mapping name")
                        .set(<interceptor>)
                        .unset(<interceptor order>));
    }
}
```
Charon contains the following request forwarding interceptors:

### Request server name rewriter
**Description:** Defines outgoing servers for incoming requests.
Load balancing can also be configured by the interceptor's API.
Default load balancer randomly chooses the outgoing host.
Custom load balancer can be created by implementing `LoadBalancer` and extending `LoadBalancerConfigurer`.\
**Set by default:** No\
**Configuration API and defaults:**
```java
import static com.github.mkopylec.charon.configuration.CharonConfigurer.charonConfiguration;
import static com.github.mkopylec.charon.forwarding.interceptors.rewrite.RandomLoadBalancerConfigurer.randomLoadBalancer;
import static com.github.mkopylec.charon.forwarding.interceptors.rewrite.RequestServerNameRewriterConfigurer.requestServerNameRewriter;

@Configuration
class CharonConfiguration {

    @Bean
    CharonConfigurer charonConfigurer() {
        return charonConfiguration()
                .set(requestServerNameRewriter()
                        .outgoingServers()
                        .loadBalancer(randomLoadBalancer()));
    }
}
```

### Regex request path rewriter
**Description:** Defines how the path of incoming requests must be changed before forwarding them to outgoing servers.
The interceptor takes an incoming request's path and matches it against configured incoming path regular expression.
If the path matches the expression then the request's path is rewrote according to the configured outgoing path template.
Incoming path regular expression can have named groups that fill outgoing path template's placeholders.
For example if incoming path regular expression is `/path/(?<group>.*)`, outgoing path template is `/other-path/<group>` and 
incoming request's path is _/path/sub-path_ then outgoing request's path will be _/other-path/sub-path_.\
**Set by default:** No\
**Configuration API and defaults:**
```java
import static com.github.mkopylec.charon.configuration.CharonConfigurer.charonConfiguration;
import static com.github.mkopylec.charon.forwarding.interceptors.rewrite.RegexRequestPathRewriterConfigurer.regexRequestPathRewriter;

@Configuration
class CharonConfiguration {

    @Bean
    CharonConfigurer charonConfigurer() {
        return charonConfiguration()
                .set(regexRequestPathRewriter()
                        .paths("/(?<path>.*)", "/<path>"));
    }
}
```

### Circuit breaker
**Description:** Applies a [circuit breaker](https://en.wikipedia.org/wiki/Circuit_breaker) design pattern to request forwarding process.
Check [here](https://resilience4j.github.io/resilience4j/#_circuitbreaker) for detailed information about circuit breaker configuration.
Charon will collect circuit breaker's metrics if a `MeterRegistry` is provided to the circuit breaker's configuration.\
**Set by default:** No\
**Configuration API and defaults:**
```java
import static com.github.mkopylec.charon.configuration.CharonConfigurer.charonConfiguration;
import static com.github.mkopylec.charon.forwarding.interceptors.resilience.CircuitBreakerConfigurer.circuitBreaker;
import static io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.custom;

@Configuration
class CharonConfiguration {

    @Bean
    CharonConfigurer charonConfigurer() {
        return charonConfiguration()
                .set(circuitBreaker()
                        .configuration(custom().recordExceptions(Throwable.class))
                        .meterRegistry(null));
    }
}
```

### Rate limiter
**Description:** Applies [rate limiting](https://en.wikipedia.org/wiki/Rate_limiting) to request forwarding process.
Check [here](https://resilience4j.github.io/resilience4j/#_ratelimiter) for detailed information about rate limiter configuration.
Charon will collect rate limiter's metrics if a `MeterRegistry` is provided to the rate limiter's configuration.\
**Set by default:** No\
**Configuration API and defaults:**
```java
import static com.github.mkopylec.charon.configuration.CharonConfigurer.charonConfiguration;
import static com.github.mkopylec.charon.forwarding.interceptors.resilience.RateLimiterConfigurer.rateLimiter;
import static io.github.resilience4j.ratelimiter.RateLimiterConfig.custom;

@Configuration
class CharonConfiguration {

    @Bean
    CharonConfigurer charonConfigurer() {
        return charonConfiguration()
                .set(rateLimiter()
                        .configuration(custom().timeoutDuration(ZERO).limitRefreshPeriod(ofSeconds(1)).limitForPeriod(100))
                        .meterRegistry(null));
    }
}
```

### Retryer
**Description:** Retries request forwarding process under the configured circumstances.
Check [here](https://resilience4j.github.io/resilience4j/#_retry) for detailed information about retryer configuration.
Charon will collect retryer's metrics if a `MeterRegistry` is provided to the retryer's configuration.\
**Set by default:** No\
**Configuration API and defaults:**
```java
import static com.github.mkopylec.charon.configuration.CharonConfigurer.charonConfiguration;
import static com.github.mkopylec.charon.forwarding.interceptors.resilience.RetryerConfigurer.retryer;
import static io.github.resilience4j.retry.RetryConfig.custom;

@Configuration
class CharonConfiguration {

    @Bean
    CharonConfigurer charonConfigurer() {
        return charonConfiguration()
                .set(retryer()
                        .configuration(custom().waitDuration(ofMillis(10)).retryOnResult(result -> ((HttpResponse) result).getStatusCode().is5xxServerError()).retryOnException(throwable -> true))
                        .meterRegistry(null));
    }
}
```

### Latency meter
**Description:** Collects latency metrics of request forwarding process if a `MeterRegistry` is provided.\
**Set by default:** No\
**Configuration API and defaults:**
```java
import static com.github.mkopylec.charon.configuration.CharonConfigurer.charonConfiguration;
import static com.github.mkopylec.charon.forwarding.interceptors.latency.LatencyMeterConfigurer.latencyMeter;

@Configuration
class CharonConfiguration {

    @Bean
    CharonConfigurer charonConfigurer() {
        return charonConfiguration()
                .set(latencyMeter()
                        .meterRegistry(null));
    }
}
```

### Asynchronous forwarder
**Description:** Forwards incoming requests asynchronously.
The forwarder does not wait for request forwarding process to finish, it immediately returns HTTP 202 Accepted response to the client.
Separate, configurable thread pool is used to forward the incoming requests.\
**Set by default:** No\
**Configuration API and defaults:**
```java
import static com.github.mkopylec.charon.configuration.CharonConfigurer.charonConfiguration;
import static com.github.mkopylec.charon.forwarding.interceptors.async.AsynchronousForwarderConfigurer.asynchronousForwarder;
import static com.github.mkopylec.charon.forwarding.interceptors.async.ThreadPoolConfigurer.threadPool;

@Configuration
class CharonConfiguration {

    @Bean
    CharonConfigurer charonConfigurer() {
        return charonConfiguration()
                .set(asynchronousForwarder()
                        .set(threadPool().coreSize(3).maximumSize(20)));
    }
}
```

### Root path response cookies rewriter
**Description:** Rewrites incoming response's cookies by setting their path to _/_.\
**Set by default:** Yes\
**Configuration API and defaults:**
```java
import static com.github.mkopylec.charon.configuration.CharonConfigurer.charonConfiguration;
import static com.github.mkopylec.charon.forwarding.interceptors.rewrite.RootPathResponseCookiesRewriterConfigurer.rootPathResponseCookiesRewriter;

@Configuration
class CharonConfiguration {

    @Bean
    CharonConfigurer charonConfigurer() {
        return charonConfiguration()
                .set(rootPathResponseCookiesRewriter());
    }
}
```

### Removing response cookies rewriter
**Description:** Removes all incoming response's cookies.\
**Set by default:** No\
**Configuration API and defaults:**
```java
import static com.github.mkopylec.charon.configuration.CharonConfigurer.charonConfiguration;
import static com.github.mkopylec.charon.forwarding.interceptors.rewrite.RemovingResponseCookiesRewriterConfigurer.removingResponseCookiesRewriter;

@Configuration
class CharonConfiguration {

    @Bean
    CharonConfigurer charonConfigurer() {
        return charonConfiguration()
                .set(removingResponseCookiesRewriter());
    }
}
```

### Request 'Host' header rewriter
**Description:** Rewrites incoming request's 'Host' header by setting its value to outgoing server name.\
**Set by default:** No\
**Configuration API and defaults:**
```java
import static com.github.mkopylec.charon.configuration.CharonConfigurer.charonConfiguration;
import static com.github.mkopylec.charon.forwarding.interceptors.rewrite.RequestHostHeaderRewriterConfigurer.requestHostHeaderRewriter;

@Configuration
class CharonConfiguration {

    @Bean
    CharonConfigurer charonConfigurer() {
        return charonConfiguration()
                .set(requestHostHeaderRewriter());
    }
}
```

### Request protocol headers rewriter
**Description:** Rewrites incoming request's protocol specific headers.
It sets 'Connection: close' header and removes 'TE' header.\
**Set by default:** Yes\
**Configuration API and defaults:**
```java
import static com.github.mkopylec.charon.configuration.CharonConfigurer.charonConfiguration;
import static com.github.mkopylec.charon.forwarding.interceptors.rewrite.RequestProtocolHeadersRewriterConfigurer.requestProtocolHeadersRewriter;

@Configuration
class CharonConfiguration {

    @Bean
    CharonConfigurer charonConfigurer() {
        return charonConfiguration()
                .set(requestProtocolHeadersRewriter());
    }
}
```

### Request proxy headers rewriter
**Description:** Adds 'X-Forwarded' headers to incoming request.
It updates the 'X-Forwarded-For' header and sets 'X-Forwarded-Proto', 'X-Forwarded-Host' and 'X-Forwarded-Port' headers.\
**Set by default:** Yes\
**Configuration API and defaults:**
```java
import static com.github.mkopylec.charon.configuration.CharonConfigurer.charonConfiguration;
import static com.github.mkopylec.charon.forwarding.interceptors.rewrite.RequestProxyHeadersRewriterConfigurer.requestProxyHeadersRewriter;

@Configuration
class CharonConfiguration {

    @Bean
    CharonConfigurer charonConfigurer() {
        return charonConfiguration()
                .set(requestProxyHeadersRewriter());
    }
}
```

### Response protocol headers rewriter
**Description:** Rewrites incoming response's protocol specific headers.
It removes 'Transfer-Encoding', 'Connection', 'Public-Key-Pins', 'Server' and 'Strict-Transport-Security' headers.\
**Set by default:** Yes\
**Configuration API and defaults:**
```java
import static com.github.mkopylec.charon.configuration.CharonConfigurer.charonConfiguration;
import static com.github.mkopylec.charon.forwarding.interceptors.rewrite.ResponseProtocolHeadersRewriterConfigurer.responseProtocolHeadersRewriter;

@Configuration
class CharonConfiguration {

    @Bean
    CharonConfigurer charonConfigurer() {
        return charonConfiguration()
                .set(responseProtocolHeadersRewriter());
    }
}
```

### Forwarding logger
**Description:** Logs information about request forwarding process.\
**Set by default:** Yes\
**Configuration API and defaults:**
```java
import static com.github.mkopylec.charon.configuration.CharonConfigurer.charonConfiguration;
import static com.github.mkopylec.charon.forwarding.interceptors.log.ForwardingLoggerConfigurer.forwardingLogger;

@Configuration
class CharonConfiguration {

    @Bean
    CharonConfigurer charonConfigurer() {
        return charonConfiguration()
                .set(forwardingLogger()
                        .successLogLevel(DEBUG)
                        .clientErrorLogLevel(INFO)
                        .serverErrorLogLevel(ERROR)
                        .unexpectedErrorLogLevel(ERROR));
    }
}
```

### Custom request forwarding interceptor
Additional interceptors can be created by implementing `RequestForwardingInterceptor` interface.
To be able to set the created interceptor globally or per request mapping `RequestForwardingInterceptorConfigurer` must be extended.
For example:
```java
class CustomInterceptor implements RequestForwardingInterceptor {

    private String property;

    void setProperty(String property) {
        this.property = property;
    }
    
    ...
}
```
```java
class CustomInterceptorConfigurer extends RequestForwardingInterceptorConfigurer<CustomInterceptor> {

    private CustomInterceptorConfigurer() {
        super(new CustomInterceptor());
    }

    static CustomInterceptorConfigurer customInterceptor() {
        return new CustomInterceptorConfigurer();
    }
    
    CustomInterceptorConfigurer property(String property) {
        configuredObject.setProperty(property);
        return this;
    }
}
```
To use the created interceptor simply set it via `CharonConfigurer` API, analogically to other interceptors:
```java
import static com.github.mkopylec.charon.configuration.CharonConfigurer.charonConfiguration;
import static mypackage.CustomInterceptorConfigurer.customInterceptor;

@Configuration
class CharonConfiguration {

    @Bean
    CharonConfigurer charonConfigurer() {
        return charonConfiguration()
                .set(customInterceptor()
                        .property("some property"));
    }
}
```

## HTTP client
The WebMVC module uses `RestTemplate` to forward requests.
Its timeouts and underlying HTTP client can be configured.
To configure the HTTP client `ClientHttpRequestFactoryCreator` needs to be implemented.
The default `RestTemplate` configuration looks like below:
```java
import static com.github.mkopylec.charon.configuration.CharonConfigurer.charonConfiguration;
import static com.github.mkopylec.charon.forwarding.RestTemplateConfigurer.restTemplate;
import static com.github.mkopylec.charon.forwarding.TimeoutConfigurer.timeout;

@Configuration
class CharonConfiguration {

    @Bean
    CharonConfigurer charonConfigurer() {
        return charonConfiguration()
                .set(restTemplate()
                        .set(timeout().connection(ofMillis(100)).read(ofMillis(1000)).write(ofMillis(1000)))
                        .set(new OkHttpRequestFactoryCreator()));
    }
}
```
The WebFlux module uses `WebClient` to forward requests.
Its timeouts and underlying HTTP client can be configured.
To configure the HTTP client `ClientHttpConnectorCreator` needs to be implemented.
The default `WebClient` configuration looks like below:
```java
import static com.github.mkopylec.charon.configuration.CharonConfigurer.charonConfiguration;
import static com.github.mkopylec.charon.forwarding.TimeoutConfigurer.timeout;
import static com.github.mkopylec.charon.forwarding.WebClientConfigurer.webClient;

@Configuration
class CharonConfiguration {

    @Bean
    CharonConfigurer charonConfigurer() {
        return charonConfiguration()
                .set(webClient()
                        .set(timeout().connection(ofMillis(100)).read(ofMillis(1000)).write(ofMillis(1000)))
                        .set(new ReactorConnectorCreator()));
    }
}
```

## Examples
See [WebMVC test application](https://github.com/mkopylec/charon-spring-boot-starter/tree/master/charon-spring-webmvc/src/test/java/com/github/mkopylec/charon/test) for more examples.\
See [WebFlux test application](https://github.com/mkopylec/charon-spring-boot-starter/tree/master/charon-spring-webmvc/src/test/java/com/github/mkopylec/charon/test) for more examples.

## License
Charon Spring Boot Starter is published under [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).
