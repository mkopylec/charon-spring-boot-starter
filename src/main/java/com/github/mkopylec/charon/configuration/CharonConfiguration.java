package com.github.mkopylec.charon.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import com.codahale.metrics.MetricRegistry;
import com.github.mkopylec.charon.core.balancer.LoadBalancer;
import com.github.mkopylec.charon.core.balancer.RandomLoadBalancer;
import com.github.mkopylec.charon.core.http.RequestDataExtractor;
import com.github.mkopylec.charon.core.http.ReverseProxyFilter;
import com.github.mkopylec.charon.core.mappings.ConfigurationMappingsProvider;
import com.github.mkopylec.charon.core.mappings.MappingsCorrector;
import com.github.mkopylec.charon.core.mappings.MappingsProvider;
import com.github.mkopylec.charon.core.retry.LoggingListener;
import com.github.mkopylec.charon.exceptions.CharonException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.Netty4ClientHttpRequestFactory;
import org.springframework.retry.RetryListener;
import org.springframework.retry.RetryOperations;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.StringUtils.appendIfMissing;

@Configuration
@EnableConfigurationProperties({CharonProperties.class, ServerProperties.class})
public class CharonConfiguration {

    @Autowired
    protected CharonProperties charon;
    @Autowired
    protected ServerProperties server;

    @Bean
    public FilterRegistrationBean charonReverseProxyFilterRegistrationBean(ReverseProxyFilter proxyFilter, MappingsProvider mappingsProvider) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(proxyFilter);
        registrationBean.setOrder(charon.getFilterOrder());
        registrationBean.setUrlPatterns(getFilterUrlPatterns(mappingsProvider));
        return registrationBean;
    }

    @Bean
    @ConditionalOnMissingBean
    public ReverseProxyFilter charonReverseProxyFilter(
            @Qualifier("charonRestOperations") RestOperations restOperations,
            @Qualifier("charonRetryOperations") RetryOperations retryOperations,
            RequestDataExtractor extractor,
            MappingsProvider mappingsProvider,
            LoadBalancer loadBalancer,
            MetricRegistry metricRegistry
    ) {
        return new ReverseProxyFilter(
                server, charon, restOperations, retryOperations, extractor, mappingsProvider, loadBalancer, metricRegistry
        );
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
    @ConditionalOnMissingBean
    public RetryOperations charonRetryOperations(@Qualifier("charonRetryListener") RetryListener listener) {
        Map<Class<? extends Throwable>, Boolean> retryableExceptions = new HashMap<>(1);
        retryableExceptions.put(Exception.class, true);
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(charon.getRetrying().getMaxAttempts(), retryableExceptions);
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(retryPolicy);
        retryTemplate.registerListener(listener);
        return retryTemplate;
    }

    @Bean
    @ConditionalOnMissingBean
    public RequestDataExtractor charonRequestDataExtractor() {
        return new RequestDataExtractor();
    }

    @Bean
    @ConditionalOnMissingBean
    public MappingsProvider charonMappingsProvider(@Qualifier("charonTaskScheduler") TaskScheduler scheduler, MappingsCorrector mappingsCorrector) {
        return new ConfigurationMappingsProvider(scheduler, charon, mappingsCorrector);
    }

    @Bean
    @ConditionalOnMissingBean
    public LoadBalancer charonLoadBalancer() {
        return new RandomLoadBalancer();
    }

    @Bean
    @ConditionalOnMissingBean
    public TaskScheduler charonTaskScheduler() {
        if (charon.getMappingsUpdate().isEnabled()) {
            ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
            scheduler.setPoolSize(2);
            return scheduler;
        }
        return null;
    }

    @Bean
    @ConditionalOnMissingBean
    public MappingsCorrector charonMappingsCorrector() {
        return new MappingsCorrector();
    }

    @Bean
    @ConditionalOnMissingBean
    public RetryListener charonRetryListener() {
        return new LoggingListener();
    }

    @Bean
    @ConditionalOnMissingBean
    public MetricRegistry charonMetricRegistry() {
        return new MetricRegistry();
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
            throw new CharonException("Invalid max number of attempts to send request value: " + maxAttempts);
        }
        if (charon.getMappingsUpdate().isEnabled()) {
            int mappingsUpdateInterval = charon.getMappingsUpdate().getIntervalInMillis();
            if (mappingsUpdateInterval < 0) {
                throw new CharonException("Invalid mappings update interval value: " + mappingsUpdateInterval);
            }
        }
    }

    protected Set<String> getFilterUrlPatterns(MappingsProvider mappingsProvider) {
        return mappingsProvider.getMappings().stream()
                .map(mapping -> appendIfMissing(mapping.getPath(), "/*"))
                .collect(toSet());
    }
}
