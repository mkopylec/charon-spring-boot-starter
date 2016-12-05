package com.github.mkopylec.charon.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

/**
 * Charon configuration properties.
 */
@ConfigurationProperties("charon")
public class CharonProperties {

    /**
     * Charon servlet filter order.
     */
    private int filterOrder = LOWEST_PRECEDENCE;
    /**
     * Properties responsible for retrying of HTTP requests forwarding.
     */
    @NestedConfigurationProperty
    private RetryingProperties retrying = new RetryingProperties();
    /**
     * Properties responsible for collecting metrics during HTTP requests forwarding.
     */
    @NestedConfigurationProperty
    private MetricsProperties metrics = new MetricsProperties();
    /**
     * Properties responsible for tracing HTTP requests proxying processes.
     */
    @NestedConfigurationProperty
    private TracingProperties tracing = new TracingProperties();
    /**
     * Properties responsible for asynchronous HTTP requests forwarding.
     */
    @NestedConfigurationProperty
    private AsynchronousForwardingThreadPoolProperties asynchronousForwardingThreadPool = new AsynchronousForwardingThreadPoolProperties();
    /**
     * Properties responsible for Hystrix circuit breaker.
     */
    @NestedConfigurationProperty
    private HystrixProperties hystrix = new HystrixProperties();
    /**
     * List of proxy mappings.
     */
    @NestedConfigurationProperty
    private List<MappingProperties> mappings = new ArrayList<>();

    public int getFilterOrder() {
        return filterOrder;
    }

    public void setFilterOrder(int filterOrder) {
        this.filterOrder = filterOrder;
    }

    public RetryingProperties getRetrying() {
        return retrying;
    }

    public void setRetrying(RetryingProperties retrying) {
        this.retrying = retrying;
    }

    public MetricsProperties getMetrics() {
        return metrics;
    }

    public void setMetrics(MetricsProperties metrics) {
        this.metrics = metrics;
    }

    public TracingProperties getTracing() {
        return tracing;
    }

    public void setTracing(TracingProperties tracing) {
        this.tracing = tracing;
    }

    public AsynchronousForwardingThreadPoolProperties getAsynchronousForwardingThreadPool() {
        return asynchronousForwardingThreadPool;
    }

    public void setAsynchronousForwardingThreadPool(AsynchronousForwardingThreadPoolProperties asynchronousForwardingThreadPool) {
        this.asynchronousForwardingThreadPool = asynchronousForwardingThreadPool;
    }

    public HystrixProperties getHystrix() {
        return hystrix;
    }

    public void setHystrix(HystrixProperties hystrix) {
        this.hystrix = hystrix;
    }

    public List<MappingProperties> getMappings() {
        return mappings;
    }

    public void setMappings(List<MappingProperties> mappings) {
        this.mappings = mappings;
    }
}
