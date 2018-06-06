package com.github.mkopylec.charon.configuration;

import com.github.mkopylec.charon.core.balancer.LoadBalancer;
import com.github.mkopylec.charon.core.balancer.RandomLoadBalancer;
import com.github.mkopylec.charon.core.http.ForwardedRequestInterceptor;
import com.github.mkopylec.charon.core.http.HttpClientProvider;
import com.github.mkopylec.charon.core.http.NoOpForwardedRequestInterceptor;
import com.github.mkopylec.charon.core.http.NoOpReceivedResponseInterceptor;
import com.github.mkopylec.charon.core.http.ReceivedResponseInterceptor;
import com.github.mkopylec.charon.core.http.RequestDataExtractor;
import com.github.mkopylec.charon.core.http.RequestForwarder;
import com.github.mkopylec.charon.core.http.ReverseProxyFilter;
import com.github.mkopylec.charon.core.mappings.ConfigurationMappingsProvider;
import com.github.mkopylec.charon.core.mappings.MappingsCorrector;
import com.github.mkopylec.charon.core.mappings.MappingsProvider;
import com.github.mkopylec.charon.core.retry.LoggingListener;
import com.github.mkopylec.charon.core.trace.LoggingTraceInterceptor;
import com.github.mkopylec.charon.core.trace.ProxyingTraceInterceptor;
import com.github.mkopylec.charon.core.trace.TraceInterceptor;
import com.github.mkopylec.charon.exceptions.CharonException;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.retry.RetryListener;
import org.springframework.retry.RetryOperations;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Configuration
@EnableConfigurationProperties(CharonProperties.class)
public class CharonConfiguration {

    protected final CharonProperties charon;
    protected final ServerProperties server;

    public CharonConfiguration(CharonProperties charon, ServerProperties server) {
        this.charon = charon;
        this.server = server;
    }

    @Bean
    public FilterRegistrationBean<ReverseProxyFilter> charonReverseProxyFilterRegistrationBean(ReverseProxyFilter proxyFilter) {
        FilterRegistrationBean<ReverseProxyFilter> registrationBean = new FilterRegistrationBean<>(proxyFilter);
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
            ProxyingTraceInterceptor traceInterceptor
    ) {
        return new ReverseProxyFilter(charon, retryOperations, defaultRetryOperations, extractor, mappingsProvider, taskExecutor, requestForwarder, traceInterceptor);
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpClientProvider charonHttpClientProvider() {
        return new HttpClientProvider();
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
    public MappingsProvider charonMappingsProvider(MappingsCorrector mappingsCorrector, HttpClientProvider httpClientProvider) {
        return new ConfigurationMappingsProvider(server, charon, mappingsCorrector, httpClientProvider);
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
            HttpClientProvider httpClientProvider,
            MappingsProvider mappingsProvider,
            LoadBalancer loadBalancer,
            Optional<MeterRegistry> meterRegistry,
            ProxyingTraceInterceptor traceInterceptor,
            ForwardedRequestInterceptor forwardedRequestInterceptor,
            ReceivedResponseInterceptor receivedResponseInterceptor
    ) {
        return new RequestForwarder(server, charon, httpClientProvider, mappingsProvider, loadBalancer, meterRegistry, traceInterceptor, forwardedRequestInterceptor, receivedResponseInterceptor);
    }

    @Bean
    @ConditionalOnMissingBean
    public RetryListener charonRetryListener() {
        return new LoggingListener(charon);
    }

    @Bean
    @ConditionalOnMissingBean
    public TraceInterceptor charonTraceInterceptor() {
        return new LoggingTraceInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean
    public ProxyingTraceInterceptor charonProxyingTraceInterceptor(TraceInterceptor traceInterceptor) {
        return new ProxyingTraceInterceptor(charon, traceInterceptor);
    }

    @Bean
    @ConditionalOnMissingBean
    public ForwardedRequestInterceptor charonForwardedRequestInterceptor() {
        return new NoOpForwardedRequestInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean
    public ReceivedResponseInterceptor charonReceivedResponseInterceptor() {
        return new NoOpReceivedResponseInterceptor();
    }

    @PostConstruct
    protected void checkConfiguration() {
        int maxAttempts = charon.getRetrying().getMaxAttempts();
        if (maxAttempts < 1) {
            throw new CharonException("Invalid max number of attempts to forward HTTP request value: " + maxAttempts);
        }
        int queueCapacity = charon.getAsynchronousForwardingThreadPool().getQueueCapacity();
        if (queueCapacity < 0) {
            throw new CharonException("Invalid asynchronous requests thread pool executor queue capacity value: " + queueCapacity);
        }
        int initialSize = charon.getAsynchronousForwardingThreadPool().getSize().getInitial();
        if (initialSize < 0) {
            throw new CharonException("Invalid asynchronous requests thread pool executor initial size value: " + initialSize);
        }
        int maximumSize = charon.getAsynchronousForwardingThreadPool().getSize().getMaximum();
        if (maximumSize < 1) {
            throw new CharonException("Invalid asynchronous requests thread pool executor maximum size value: " + maximumSize);
        }
        if (initialSize > maximumSize) {
            throw new CharonException("Initial size of asynchronous requests thread pool executor value: " + initialSize + " greater than maximum size value: " + maximumSize);
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

    protected boolean isAsynchronousMappingPresent() {
        return !charon.getMappings().stream()
                .filter(MappingProperties::isAsynchronous)
                .collect(toList())
                .isEmpty();
    }
}
