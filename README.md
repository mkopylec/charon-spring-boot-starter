# Charon Spring Boot Starter
[![Build Status](https://travis-ci.org/mkopylec/charon-spring-boot-starter.svg?branch=master)](https://travis-ci.org/mkopylec/charon-spring-boot-starter)
[![Coverage Status](https://coveralls.io/repos/mkopylec/charon-spring-boot-starter/badge.svg?branch=master&service=github&dummy=1)](https://coveralls.io/github/mkopylec/charon-spring-boot-starter?branch=master&dummy=1)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mkopylec/charon-spring-boot-starter/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/com.github.mkopylec/charon-spring-boot-starter)

Charon is a **reverse proxy** implementation.
It automatically forwards HTTP requests from one web application to another and sends back the received HTTP response to the client.
There are some alternative reverse proxy implementations like: [Zuul](https://github.com/Netflix/zuul/wiki) or [Smiley's HTTP Proxy Servlet](https://github.com/mitre/HTTP-Proxy-Servlet).
This tool tries to get the best of them joining their features into a one Spring Boot starter.

## Features
- easy to use
- highly configurable and extensible
- retrying support based on [Spring Retry](http://docs.spring.io/spring-batch/reference/html/retry.html)
- metrics support based on [Metrics](http://metrics.dropwizard.io/)
- customizable proxy mappings changeable at runtime
- customizable load balancer
- forward HTTP headers support
- intercepting and tracing

## Contribution
Please use **pull-requests branch** as a destination branch for all pull requests.

## Migrating from 1.x.x to 2.x.x

- remove `@EnableCharon` annotation
- correct `charon.mertics` properties in the _application.yml_ file if collecting metrics is enabled
- `charon.timeout` properties are no longer available in the _application.yml_ file, now timeouts can be set per mapping

## Installing

```gradle
repositories {
    mavenCentral()
}
dependencies {
    compile group: 'com.github.mkopylec', name: 'charon-spring-boot-starter', version: '2.4.0'
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

Configure proxy mappings in _application.yml_ file:

```yaml
charon.mappings:
    -
        name: sample mapping
        path: /some/path
        destinations: http://firsthost:8080, http://secondhost:8081
```

When an application is configured like above then every request to _/some/path/endpoint/**_
will be forwarded to _http://firsthost:8080/endpoint/**_ or _http://secondhost:8081/endpoint/**_.
Note that the mapped path _/some/path_ is stripped from the forwarded request URL.
Also note that the `charon.mappings` property's value is a list, therefore more mappings can be set.

## Advanced usage
Charon can be configured in many ways. This can be done via configuration properties in _application.yml_ file or by creating Spring beans.

### Mappings
Mappings define how Charon will forward incoming HTTP requests.
Every mapping must be named.
By default the mapped path is stripped from the forward request URL.
To preserve the mapped path in the URL set an appropriate configuration property:

```yaml
charon.mappings:
    -
        ...
        strip-path: false
```

If the mappings configuration using configuration properties is not enough, a custom mappings provider can be created.
This can be done by creating a Spring bean of type `MappingsProvider`:

```java
@Component
@EnableConfigurationProperties({CharonProperties.class, ServerProperties.class})
public class CustomMappingsProvider extends MappingsProvider {

    @Autowired
    public CustomMappingsProvider(ServerProperties server, CharonProperties charon, MappingsCorrector mappingsCorrector) {
        super(server, charon, mappingsCorrector);
    }
	
    @Override
    protected boolean shouldUpdateMappings(HttpServletRequest request) {
        ...
    }

    @Override
    protected List<Mapping> retrieveMappings() {
        ...
    }
}
```

Using custom `MappingsProvider` the mappings are populated during application startup using `retrieveMappings` method.
The mappings are also updated during application runtime every time the `shouldUpdateMappings` method returns `true`.

### Retrying
By default there is only one attempt to forward request.
Forward request retrying can be enabled for each mapping by setting an appropriate configuration property:

```yaml
charon.mappings:
    -
        ...
        retryable: true
```

By default a next try is triggered by any `Exception` that occurs during proxying process and also by 5xx HTTP responses from destination hosts.
To change the retryable exceptions set an appropriate configuration property:

```yaml
charon.retrying.retry-on.exceptions: <comma-separated list fo exceptions>
```

To disable retrying on 5xx HTTP responses set an appropriate configuration property:

```yaml
charon.retrying.retry-on.server-http-error: false
```

Retrying on 4xx HTTP responses from destination hosts is disabled by default.
To turn it on set an appropriate configuration property:

```yaml
charon.retrying.retry-on.client-http-error: true
```

If retrying is enabled there are maximum three attempts to forward request.
To change the maximum number of attempts set an appropriate configuration property:

```yaml
charon.retrying.max-attempts: <number_of_tries>
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

### Metrics
Collecting performance metrics is disabled by default.
To turn them on set an appropriate configuration property:

```yaml
charon.metrics.enabled: true
```

Charon collects metrics per mapping.
To report collected metrics a reporter is needed.
Charon includes two metrics reporters but they are disabled by default.
To enable a reporter that logs collected metrics set an appropriate configuration property:

```yaml
charon.metrics.reporting.logger.enabled: true
```

To enable a reporter that sends collected metrics to a [Graphite](https://graphiteapp.org/) server set an appropriate configuration properties:

```yaml
charon.metrics.reporting.graphite:
    enabled: true
    hostname: <graphite_hostname>
    port: <graphite_port>
```

The default metrics reporters report results every 60 seconds.
The interval can be changed by setting an appropriate configuration property:

```yaml
charon.metrics.reporting.interval-in-seconds: <interval_in_seconds>
```

To create a custom metrics reporter create a Spring bean that extends `ScheduledReporter`:

```java
@Component
public class CustomMetricsReporter extends ScheduledReporter {

    @Autowired
    public CustomMetricsReporter(MetricRegistry registry) {
        ...
    }

    @Override
    public void report(SortedMap<String, Gauge> gauges, SortedMap<String, Counter> counters, SortedMap<String, Histogram> histograms, SortedMap<String, Meter> meters, SortedMap<String, Timer> timers) {
        ...
    }
    
    @PostConstruct
    private void startCapturingMetrics() {
        start(...);
    }
}
```

### Forwarded request intercepting
Charon gives a possibility to change the outgoing HTTP requests.
Particularly any aspect of the request can be modified: method, URI, headers and body.
To intercept requests create a Spring bean of type `ForwardedRequestInterceptor` and modify the `RequestData` object:

```java
@Component
public class CustomForwardedRequestInterceptor implements ForwardedRequestInterceptor {

    @Override
    public void intercept(RequestData data) {
        ...
    }
}
```

### Received response intercepting
Charon gives a possibility to change the incoming HTTP responses.
Particularly any aspect of the response can be modified: status, headers and body.
To intercept responses create a Spring bean of type `ReceivedResponseInterceptor` and modify the `ResponseData` object:

```java
@Component
public class CustomReceivedResponseInterceptor implements ReceivedResponseInterceptor {

    @Override
    public void intercept(ResponseData data) {
        ...
    }
}
```

### Tracing
Charon can trace a proxying process.
Tracing allows applications to collect detailed information about Charon's activity.
To enable it set an appropriate configuration property:

```yaml
charon.tracing.enabled: true
```

Every trace collects information at five checkpoints:

- request received - captures an incoming HTTP request
- no mapping found - captures an incoming HTTP request that will not be forwarded to any destination host
- forward start - captures an HTTP request that will be sent to the destination host
- forward error - captures an exception thrown while sending an HTTP request to destination host
- forward complete - captures an HTTP response received from the destination host

In each checkpoint a trace ID and current HTTP data are captured.
A trace ID remains the same throughout the whole proxying process of a single HTTP request, therefore trace checkpoints can be joined by trace IDs value.
By default, when tracing is enabled, Charon logs the captured information.
To change this behaviour create a Spring bean of type `TraceInterceptor`:

```java
@Component
public class CustomTraceInterceptor implements TraceInterceptor {

    @Override
    public void onRequestReceived(String traceId, IncomingRequest request) {
        ...
    }
    
    @Override
    public void onNoMappingFound(String traceId, IncomingRequest request) {
        ...
    }

    @Override
    public void onForwardStart(String traceId, ForwardRequest request) {
        ...
    }

    @Override
    public void onForwardError(String traceId, Throwable error) {
        ...
    }

    @Override
    public void onForwardComplete(String traceId, ReceivedResponse response) {
        ...
    }
}
```

### Asynchronous forwarding
By default HTTP requests are forwarded synchronously.
Forwarding process can also be asynchronous.
In asynchronous mode a response is returned immediately with 202 (accepted) HTTP status.
To enable asynchronous forwarding set an appropriate configuration property:

```yaml
charon.mappings:
    -
        ...
        asynchronous: true
```

For asynchronous forwarding a thread pool executor is used.
Its configuration is exposed by `charon.asynchronous-forwarding-thread-pool` configuration properties.

### Other tips
- change the logging level of `com.github.mkopylec.charon` to DEBUG to see what's going on under the hood
- tracing logs have INFO and ERROR level
- check the [`CharonConfiguration`](https://github.com/mkopylec/charon-spring-boot-starter/blob/master/src/main/java/com/github/mkopylec/charon/configuration/CharonConfiguration.java) to see what else can be overridden by creating a Spring bean
- if the incoming HTTP request cannot be mapped to any path it will be normally handled by the web application
- mapping destinations can have custom schemes; when a destination is lack of a scheme part the _http://_ will be prepended
- X-Forwarded-For, X-Forwarded-Proto, X-Forwarded-Host and X-Forwarded-Port headers are added to every forwarded request
- the proxy is based on a servlet filter, the order of the filter is configurable
- do not prepend server context path to mappings paths, it will be done automatically
- if there are mappings like _/uri_ and _/uri/path_ and the incoming request URI is _/uri/path/something_ than the more specific mapping (_/uri/path_) will be chosen
- proxying requests to domains with trusted SSL certificates is automatically supported

## Configuration properties list
The following list contains all available configuration properties with their default values.

```yaml
charon:
    filter-order: Ordered.LOWEST_PRECEDENCE # Charon servlet filter order.
    retrying:
        max-attempts: 3 # Maximum number of HTTP request forward tries.
        retry-on:
            client-http-error: false # Flag for enabling and disabling triggering HTTP requests forward retries on 4xx HTTP responses from destination.
            server-http-error: true # Flag for enabling and disabling triggering HTTP requests forward retries on 5xx HTTP responses from destination.
            exceptions: java.lang.Exception # Comma-separated list of exceptions that triggers HTTP request forward retries.
    metrics:
        enabled: false # Flag for enabling and disabling collecting metrics during HTTP requests forwarding.
        names-prefix: charon # Global metrics names prefix.
        reporting:
            interval-in-seconds: 60 # Metrics reporting interval in seconds.
            logger:
                enabled: false # Flag for enabling and disabling reporting metrics to application logger.
            graphite:
                enabled: false # Flag for enabling and disabling reporting metrics to Graphite server.
                hostname: # Graphite server hostname.
                port: 2003 # Graphite server port.
    tracing:
        enabled: false # Flag for enabling and disabling tracing HTTP requests proxying processes.
    asynchronous-forwarding-thread-pool:
        queue-capacity: 50 # Thread pool executor queue capacity.
        size:
            initial: 5 # Thread pool executor initial number of threads.
            maximum: 30 # Thread pool executor maximum number of threads.
    mappings:
        -
            name: # Name of the mapping.
            path: / # Path for mapping incoming HTTP requests URIs.
            destinations: # List of destination hosts where HTTP requests will be forwarded.
            asynchronous: false # Flag for enabling and disabling asynchronous HTTP request forwarding.
            strip-path: true # Flag for enabling and disabling mapped path stripping from forwarded request URI.
            retryable: false # Flag for enabling and disabling retrying of HTTP requests forwarding.
            timeout:
                connect: 200 # Connect timeout for HTTP requests forwarding.
                read: 2000 # Read timeout for HTTP requests forwarding.
```

## Examples
See [test application](https://github.com/mkopylec/charon-spring-boot-starter/tree/master/src/test/java/com/github/mkopylec/charon/application) for more examples.

## License
Charon Spring Boot Starter is published under [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).
