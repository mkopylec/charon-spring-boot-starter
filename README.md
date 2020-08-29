# Charon Spring Boot Starter
[![Build Status](https://travis-ci.org/mkopylec/charon-spring-boot-starter.svg?branch=master)](https://travis-ci.org/mkopylec/charon-spring-boot-starter)
[![Code Coverage](https://codecov.io/gh/mkopylec/charon-spring-boot-starter/branch/master/graph/badge.svg)](https://codecov.io/gh/mkopylec/charon-spring-boot-starter)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mkopylec/charon-spring-webmvc/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/com.github.mkopylec/charon-spring-webmvc)

Charon is a **reverse proxy** implementation.
It automatically forwards HTTP requests from one HTTP server to another and sends back the received HTTP response to the client.
There are some alternative reverse proxy implementations like [Zuul](https://github.com/Netflix/zuul/wiki) or [Smiley's HTTP Proxy Servlet](https://github.com/mitre/HTTP-Proxy-Servlet).
Zuul is highly bounded to [Spring Cloud Netflix](https://spring.io/projects/spring-cloud-netflix), Smiley's HTTP Proxy Servlet is a simple one, without advanced features.
Charon is a universal Spring Boot tool. It already has a lot of features implemented and its architecture provides an easy way to add new ones.

## Features
- highly configurable and extensible
- Spring [WebMVC](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html) and [WebFlux](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html) support
- multiple request forwarding mappings
- load balancing
- flexible path rewriting
- [Resilience4j](https://resilience4j.github.io/resilience4j/) support
- metrics based on [Micrometer](https://micrometer.io/)
- asynchronous request forwarding
- authentication
- cookies rewriting
- 'X-Forwarded' HTTP headers support
- forwarding process intercepting
- configurable HTTP client

## Migrating from older versions to 4.x.x
Charon was completely rewritten, configuration via _application.yml_ file is no longer available.
Now Charon can be configured by in-code configuration.
See the documentation for more details. 

## Documentation
Full documentation is located [here](https://github.com/mkopylec/charon-spring-boot-starter/wiki).

## License
Charon Spring Boot Starter is published under [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).
