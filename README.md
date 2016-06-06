# Reverse Proxy Spring Boot Starter
[![Build Status](https://travis-ci.org/mkopylec/reverse-proxy-spring-boot-starter.svg?branch=master)](https://travis-ci.org/mkopylec/reverse-proxy-spring-boot-starter)
[![Coverage Status](https://coveralls.io/repos/mkopylec/reverse-proxy-spring-boot-starter/badge.svg?branch=master&service=github)](https://coveralls.io/github/mkopylec/reverse-proxy-spring-boot-starter?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mkopylec/reverse-proxy-spring-boot-starter/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/com.github.mkopylec/reverse-proxy-spring-boot-starter)

The starter automatically forwards HTTP requests from one web application to another and sends back the received HTTP response to the client.
There are some alternative reverse proxy implementations like: [Zuul](https://github.com/Netflix/zuul/wiki) or [Smiley's HTTP Proxy Servlet](https://github.com/mitre/HTTP-Proxy-Servlet).
This tool tries to get the best of them joining their features into a one Spring Boot starter.

## Features
- easy to use
- highly configurable and extensible
- NIO support based on [Netty](http://netty.io/)
- retrying support based on [Spring Retry](http://docs.spring.io/spring-batch/reference/html/retry.html)
- customizable proxy mappings
- customizable load balancer
- resilient to destination hosts changes during runtime
- forward HTTP headers support

## Installing

```gradle
repositories {
    mavenCentral()
}
dependencies {
    compile 'com.github.mkopylec:reverse-proxy-spring-boot-starter:1.1.2'
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
        destinations: http://firsthost:8080, http://secondhost:8081
```

When an application is configured like above then every request to _/some/path/endpoint/**_
will be forwarded to _http://firsthost:8080/endpoint/**_ or _http://secondhost:8081/endpoint/**_.
Note that the mapped path _/some/path_ is stripped from the forwarded request URL.
Also note that the `reverse-proxy.mappings` property's value is a list, therefore more mappings can be set.

## Advanced usage
The starter can be configured in many ways. This can be done via configuration properties in _application.yml_ file or by creating Spring beans.

### Mapped path stripping
By default the mapped path is stripped from the forward request URL.
To preserve the mapped path in the URL set an appropriate configuration property:

```yaml
reverse-proxy.mappings:
    -
        ...
        strip-path: false
```

### Retrying
By default there are maximum 3 tries to make a forward request. The next try is triggered when a non-HTTP error occurs.
This means that the 4xx and 5xx responses from destination hosts will not trigger a next try.
To change the maximum number of attempts set an appropriate configuration property:

```yaml
reverse-proxy.retrying.max-attempts: <number_of_tries>
```

### Mappings provider
If the mappings configuration using configuration properties is not enough, a custom mappings provider can be created.
This can done by creating a Spring bean of type `MappingsProvider`:

```java
@Component
@EnableConfigurationProperties(ReverseProxyProperties.class)
public class CustomMappingsProvider extends MappingsProvider {

    @Autowired
	public CustomMappingsProvider(TaskScheduler scheduler, ReverseProxyProperties reverseProxy, MappingsCorrector mappingsCorrector) {
		super(scheduler, reverseProxy, mappingsCorrector);
	}

	@Override
	protected List<Mapping> retrieveMappings() {
		...
	}
}
```

### Load balancer
The default load balancer randomly chooses a destination host from the available list.
A custom load balancer can be created by creating a Spring bean of type `LoadBalancer`:

```java
@Component
public class CustomLoadBalancer implements LoadBalancer {

	@Override
	public String chooseDestination(List<String> destinations) {
		...
	}
}
```

### Mappings update
The starter is resilient to mappings changes during application runtime.
By default the mappings are updated when a non-HTTP error occurs while forwarding a HTTP request.
This means that the 4xx and 5xx responses from destination hosts will not trigger the mappings update.
This behaviour can be changed by setting an appropriate configuration property:

```yaml
reverse-proxy.mappings-update.on-non-http-error: false
```

Besides that the mappings are automatically updated every 30 seconds by default.
To change the interval of updates set an appropriate configuration property:

```yaml
reverse-proxy.mappings-update.interval-in-millis: <interval_in_milliseconds>
```

### Other tips
- change the logging levels of `com.github.mkopylec.reverseproxy` and `org.springframework.retry` to DEBUG or TRACE to see what's going on under the hood
- check the [`ReverseProxyConfiguration`](https://github.com/mkopylec/reverse-proxy-spring-boot-starter/blob/master/src/main/java/com/github/mkopylec/reverseproxy/configuration/ReverseProxyConfiguration.java) to see what else can be overridden by creating a Spring bean
- if the incoming HTTP request cannot be mapped to any path it will be normally handled by the web application
- mapping destinations can have custom schemes; when a destination is lack of a scheme part the _http://_ will be prepended
- X-Forwarded-For, X-Forwarded-Proto, X-Forwarded-Host and X-Forwarded-Port headers are added to every forwarded request
- to turn off automatic mappings updates set the interval to 0
- turn off all mappings updates (automatic and triggered by error) if they are not needed
- the proxy is based on a servlet filter, the order of the filter is configurable
- do not prepend server context path to mappings paths, it will be done automatically

## Configuration properties list

```yaml
reverse-proxy:
    filter-order: Ordered.LOWEST_PRECEDENCE # Reverse proxy servlet filter order.
    timeout:
        connect: 500 # Connect timeout for HTTP requests forwarding.
        read: 2000 # Read timeout for HTTP requests forwarding.
    retrying:
        max-attempts: 3 # Maximum number of HTTP request forward tries.
    mappings-update:
        enabled: false # Flag for enabling and disabling mappings updates.
        on-non-http-error: true # Flag for enabling and disabling triggering mappings updates on non-HTTP errors occurred during HTTP requests forwarding.
        interval-in-millis: 30000 # Interval in milliseconds between automatic mappings updates.
    mappings:
        -
            path: / # Path for mapping incoming HTTP requests URIs.
            destinations: # List of destination hosts where HTTP requests will be forwarded.
            strip-path: true # Flag for enabling and disabling mapped path stripping from forwarded request URI.
```

## Examples
See [test application](https://github.com/mkopylec/reverse-proxy-spring-boot-starter/tree/master/src/test/java/com/github/mkopylec/reverseproxy/application) for more examples.

## License
Reverse Proxy Spring Boot Starter is published under [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).
