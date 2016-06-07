package com.github.mkopylec.reverseproxy.configuration;

import com.github.mkopylec.reverseproxy.core.balancer.LoadBalancer;
import com.github.mkopylec.reverseproxy.core.balancer.RandomLoadBalancer;
import com.github.mkopylec.reverseproxy.core.http.HttpProxyFilter;
import com.github.mkopylec.reverseproxy.core.http.RequestDataExtractor;
import com.github.mkopylec.reverseproxy.core.mappings.ConfigurationMappingsProvider;
import com.github.mkopylec.reverseproxy.core.mappings.MappingsCorrector;
import com.github.mkopylec.reverseproxy.core.mappings.MappingsProvider;
import com.github.mkopylec.reverseproxy.exceptions.ReverseProxyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.Netty4ClientHttpRequestFactory;
import org.springframework.retry.RetryOperations;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.StringUtils.appendIfMissing;

@Configuration
@EnableConfigurationProperties({ReverseProxyProperties.class, ServerProperties.class})
public class ReverseProxyConfiguration {

    @Autowired
    protected ReverseProxyProperties reverseProxy;
    @Autowired
    protected ServerProperties server;
    @Qualifier("rpTaskScheduler")
    @Autowired(required = false)
    protected TaskScheduler scheduler;

    @Bean
    public FilterRegistrationBean rpHttpProxyFilterRegistrationBean(HttpProxyFilter proxyFilter, MappingsProvider mappingsProvider) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(proxyFilter);
        registrationBean.setOrder(reverseProxy.getFilterOrder());
        registrationBean.setUrlPatterns(getFilterUrlPatterns(mappingsProvider));
        return registrationBean;
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpProxyFilter rpHttpProxyFilter(
            @Qualifier("rpRestOperations") RestOperations restOperations,
            @Qualifier("rpRetryOperations") RetryOperations retryOperations,
            RequestDataExtractor extractor,
            MappingsProvider mappingsProvider,
            LoadBalancer loadBalancer
    ) {
        return new HttpProxyFilter(server, reverseProxy, restOperations, retryOperations, extractor, mappingsProvider, loadBalancer);
    }

    @Bean
    @ConditionalOnMissingBean
    public RestOperations rpRestOperations() {
        Netty4ClientHttpRequestFactory requestFactory = new Netty4ClientHttpRequestFactory();
        requestFactory.setConnectTimeout(reverseProxy.getTimeout().getConnect());
        requestFactory.setReadTimeout(reverseProxy.getTimeout().getRead());
        return new RestTemplate(requestFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public RetryOperations rpRetryOperations() {
        Map<Class<? extends Throwable>, Boolean> retryableExceptions = new HashMap<>(1);
        retryableExceptions.put(Exception.class, true);
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(reverseProxy.getRetrying().getMaxAttempts(), retryableExceptions);
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(retryPolicy);
        return retryTemplate;
    }

    @Bean
    @ConditionalOnMissingBean
    public RequestDataExtractor rpRequestDataExtractor() {
        return new RequestDataExtractor();
    }

    @Bean
    @ConditionalOnMissingBean
    public MappingsProvider rpMappingsProvider(MappingsCorrector mappingsCorrector) {
        return new ConfigurationMappingsProvider(scheduler, reverseProxy, mappingsCorrector);
    }

    @Bean
    @ConditionalOnMissingBean
    public LoadBalancer rpLoadBalancer() {
        return new RandomLoadBalancer();
    }

    @Bean
    @ConditionalOnMissingBean
    public TaskScheduler rpTaskScheduler() {
        if (reverseProxy.getMappingsUpdate().isEnabled()) {
            ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
            scheduler.setPoolSize(2);
            return scheduler;
        }
        return null;
    }

    @Bean
    @ConditionalOnMissingBean
    public MappingsCorrector rpMappingsCorrector() {
        return new MappingsCorrector();
    }

    @PostConstruct
    protected void checkConfiguration() {
        int connectTimeout = reverseProxy.getTimeout().getConnect();
        int readTimeout = reverseProxy.getTimeout().getRead();
        if (connectTimeout < 0) {
            throw new ReverseProxyException("Invalid connect timeout value: " + connectTimeout);
        }
        if (readTimeout < 0) {
            throw new ReverseProxyException("Invalid read timeout value: " + readTimeout);
        }
        int maxAttempts = reverseProxy.getRetrying().getMaxAttempts();
        if (maxAttempts < 1) {
            throw new ReverseProxyException("Invalid max number of attempts to send request value: " + maxAttempts);
        }
        if (reverseProxy.getMappingsUpdate().isEnabled()) {
            int mappingsUpdateInterval = reverseProxy.getMappingsUpdate().getIntervalInMillis();
            if (mappingsUpdateInterval < 0) {
                throw new ReverseProxyException("Invalid mappings update interval value: " + mappingsUpdateInterval);
            }
        }
    }

    protected Set<String> getFilterUrlPatterns(MappingsProvider mappingsProvider) {
        return mappingsProvider.getMappings().stream()
                .map(mapping -> appendIfMissing(mapping.getPath(), "/*"))
                .collect(toSet());
    }
}
