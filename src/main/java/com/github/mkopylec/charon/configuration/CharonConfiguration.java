package com.github.mkopylec.charon.configuration;

import com.codahale.metrics.MetricRegistry;
import com.github.mkopylec.charon.configuration.CharonProperties.Mapping;
import com.github.mkopylec.charon.core.balancer.LoadBalancer;
import com.github.mkopylec.charon.core.balancer.RandomLoadBalancer;
import com.github.mkopylec.charon.core.http.RequestDataExtractor;
import com.github.mkopylec.charon.core.http.RequestForwarder;
import com.github.mkopylec.charon.core.http.ReverseProxyFilter;
import com.github.mkopylec.charon.core.mappings.ConfigurationMappingsProvider;
import com.github.mkopylec.charon.core.mappings.MappingsCorrector;
import com.github.mkopylec.charon.core.mappings.MappingsProvider;
import com.github.mkopylec.charon.core.retry.LoggingListener;
import com.github.mkopylec.charon.core.trace.LoggingTraceInterceptor;
import com.github.mkopylec.charon.core.trace.TraceInterceptor;
import com.github.mkopylec.charon.exceptions.CharonException;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.client.Netty4ClientHttpRequestFactory;
import org.springframework.retry.RetryListener;
import org.springframework.retry.RetryOperations;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codahale.metrics.Slf4jReporter.LoggingLevel.TRACE;
import static com.codahale.metrics.Slf4jReporter.forRegistry;
import static java.util.Collections.emptyList;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;

@Configuration
@EnableMetrics
@EnableConfigurationProperties({CharonProperties.class, ServerProperties.class})
public class CharonConfiguration extends MetricsConfigurerAdapter {

    @Autowired
    protected CharonProperties charon;
    @Autowired
    protected ServerProperties server;
    @Autowired
    protected MetricRegistry metricRegistry;

    @Bean
    public FilterRegistrationBean charonReverseProxyFilterRegistrationBean(ReverseProxyFilter proxyFilter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(proxyFilter);
        registrationBean.setOrder(charon.getFilterOrder());
        return registrationBean;
    }

    @Bean
    @ConditionalOnMissingBean
    public ReverseProxyFilter charonReverseProxyFilter(
            @Qualifier("charonRetryOperations") RetryOperations retryOperations,
            @Qualifier("charonDefaultRetryOperations") RetryOperations defaultRetryOperations,
            RequestDataExtractor extractor,
            MappingsProvider mappingsProvider,
            @Qualifier("charonTaskExecutor") TaskExecutor taskExecutor,
            RequestForwarder requestForwarder,
            TraceInterceptor traceInterceptor
    ) {
        return new ReverseProxyFilter(charon, retryOperations, defaultRetryOperations, extractor, mappingsProvider, taskExecutor, requestForwarder, traceInterceptor);
    }

    @Bean
    @ConditionalOnMissingBean
    public RestOperations charonRestOperations() {
        Netty4ClientHttpRequestFactory requestFactory = new Netty4ClientHttpRequestFactory();
        requestFactory.setConnectTimeout(charon.getTimeout().getConnect());
        requestFactory.setReadTimeout(charon.getTimeout().getRead());
        return new RestTemplate(requestFactory);
    }

    @Bean
    @ConditionalOnMissingBean(name = "charonRetryOperations")
    public RetryOperations charonRetryOperations(@Qualifier("charonRetryListener") RetryListener listener) {
        return createRetryOperations(listener, charon.getRetrying().getMaxAttempts(), charon.getRetrying().getRetryOn().getExceptions());
    }

    @Bean
    @ConditionalOnMissingBean(name = "charonDefaultRetryOperations")
    public RetryOperations charonDefaultRetryOperations(@Qualifier("charonRetryListener") RetryListener listener) {
        return createRetryOperations(listener, 1, emptyList());
    }

    @Bean
    @ConditionalOnMissingBean
    public RequestDataExtractor charonRequestDataExtractor() {
        return new RequestDataExtractor();
    }

    @Bean
    @ConditionalOnMissingBean
    public MappingsProvider charonMappingsProvider(MappingsCorrector mappingsCorrector) {
        return new ConfigurationMappingsProvider(server, charon, mappingsCorrector);
    }

    @Bean
    @ConditionalOnMissingBean
    public LoadBalancer charonLoadBalancer() {
        return new RandomLoadBalancer();
    }

    @Bean
    @ConditionalOnMissingBean
    public MappingsCorrector charonMappingsCorrector() {
        return new MappingsCorrector();
    }

    @Bean
    @ConditionalOnMissingBean
    public TaskExecutor charonTaskExecutor() {
        if (isAsynchronousMappingPresent()) {
            ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
            taskExecutor.setQueueCapacity(charon.getAsynchronousForwardingThreadPool().getQueueCapacity());
            taskExecutor.setCorePoolSize(charon.getAsynchronousForwardingThreadPool().getSize().getInitial());
            taskExecutor.setMaxPoolSize(charon.getAsynchronousForwardingThreadPool().getSize().getMaximum());
            return taskExecutor;
        }
        return null;
    }

    @Bean
    @ConditionalOnMissingBean
    public RequestForwarder charonRequestForwarder(
            @Qualifier("charonRestOperations") RestOperations restOperations,
            MappingsProvider mappingsProvider,
            LoadBalancer loadBalancer,
            TraceInterceptor traceInterceptor
    ) {
        return new RequestForwarder(server, charon, restOperations, mappingsProvider, loadBalancer, metricRegistry, traceInterceptor);
    }

    @Bean
    @ConditionalOnMissingBean
    public RetryListener charonRetryListener() {
        return new LoggingListener(charon);
    }

    @Bean
    @ConditionalOnMissingBean
    public TraceInterceptor charonTraceInterceptor() {
        if (charon.getTracing().isEnabled()) {
            return new LoggingTraceInterceptor();
        }
        return null;
    }

    @PostConstruct
    protected void checkConfiguration() {
        int connectTimeout = charon.getTimeout().getConnect();
        int readTimeout = charon.getTimeout().getRead();
        if (connectTimeout < 0) {
            throw new CharonException("Invalid connect timeout value: " + connectTimeout);
        }
        if (readTimeout < 0) {
            throw new CharonException("Invalid read timeout value: " + readTimeout);
        }
        int maxAttempts = charon.getRetrying().getMaxAttempts();
        if (maxAttempts < 1) {
            throw new CharonException("Invalid max number of attempts to forward HTTP request value: " + maxAttempts);
        }
        int queueCapacity = charon.getAsynchronousForwardingThreadPool().getQueueCapacity();
        if (queueCapacity < 0) {
            throw new CharonException("Invalid thread pool executor queue capacity value: " + queueCapacity);
        }
        int initialSize = charon.getAsynchronousForwardingThreadPool().getSize().getInitial();
        if (initialSize < 0) {
            throw new CharonException("Invalid thread pool executor initial size value: " + initialSize);
        }
        int maximumSize = charon.getAsynchronousForwardingThreadPool().getSize().getMaximum();
        if (maximumSize < 1) {
            throw new CharonException("Invalid thread pool executor maximum size value: " + maximumSize);
        }
        if (initialSize > maximumSize) {
            throw new CharonException("Initial size of thread pool executor value: " + initialSize + " greater than maximum size value: " + maximumSize);
        }
        if (shouldCreateDefaultMetricsReporter()) {
            registerReporter(forRegistry(metricRegistry)
                    .convertDurationsTo(MILLISECONDS)
                    .convertRatesTo(SECONDS)
                    .withLoggingLevel(TRACE)
                    .outputTo(getLogger(ReverseProxyFilter.class))
                    .build()
            ).start(charon.getMetrics().getLoggingReporter().getReportingIntervalInSeconds(), SECONDS);
        }
    }

    protected RetryOperations createRetryOperations(RetryListener listener, int maxAttempts, List<Class<? extends Throwable>> retryableErrors) {
        Map<Class<? extends Throwable>, Boolean> retryableExceptions = new HashMap<>(retryableErrors.size());
        retryableErrors.forEach(error -> retryableExceptions.put(error, true));
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(maxAttempts, retryableExceptions);
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(retryPolicy);
        retryTemplate.registerListener(listener);
        return retryTemplate;
    }

    protected boolean shouldCreateDefaultMetricsReporter() {
        return charon.getMetrics().isEnabled() && charon.getMetrics().getLoggingReporter().isEnabled();
    }

    protected boolean isAsynchronousMappingPresent() {
        return !charon.getMappings().stream()
                .filter(Mapping::isAsynchronous)
                .collect(toList())
                .isEmpty();
    }
}
