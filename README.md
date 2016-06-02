# Reverse Proxy Spring Boot Starter
[![Build Status](https://travis-ci.org/mkopylec/reverse-proxy-spring-boot-starter.svg?branch=master)](https://travis-ci.org/mkopylec/reverse-proxy-spring-boot-starter)
[![Coverage Status](https://coveralls.io/repos/mkopylec/reverse-proxy-spring-boot-starter/badge.svg?branch=master&service=github)](https://coveralls.io/github/mkopylec/reverse-proxy-spring-boot-starter?branch=master)

The starter automatically forwards HTTP requests from one web application to another and sends back the received HTTP response to the client.
There are some alternative reverse proxy implementations like: [Zuul](https://github.com/Netflix/zuul/wiki) or [Smiley's HTTP Proxy Servlet](https://github.com/mitre/HTTP-Proxy-Servlet).
This tool tries to get the best of them joining their features into a one Spring Boot starter.

## Features
- easy to use
- highly configurable and extensible
- NIO support based on [Netty](http://netty.io/)
- retrying support based on [Spring Retry](http://docs.spring.io/spring-batch/reference/html/retry.html)
- customizable proxy mappings provider
- customizable load balancer
- resilient to destination hosts changes during runtime
- X-Forwarded-For header support

## Installing

```gradle
repositories {
    mavenCentral()
}
dependencies {
    compile 'com.github.mkopylec:reverse-proxy-spring-boot-starter:1.0.0'
}
```

## Basic usage
Add `@EnableReverseProxy` to a Spring Boot web application:

```java
@EnableReverseProxy
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

Configure proxy mappings in _application.yml_ file:

```yaml
reverse-proxy.mappings:
    -
        path: /some/path
        destinations: firsthost:8080, secondhost:8081
```

When an application is configured like above then every request to _/some/path/endpoint/**_
will be forwarded to _http://firsthost:8080/endpoint/**_ or _http://secondhost:8081/endpoint/**_.
Note that the mapped path _/some/path_ is stripped from the forwarded request URL.

## License
Reverse Proxy Spring Boot Starter is published under [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).
