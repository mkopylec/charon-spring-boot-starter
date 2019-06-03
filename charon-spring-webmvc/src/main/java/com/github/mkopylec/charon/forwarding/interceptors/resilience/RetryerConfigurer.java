package com.github.mkopylec.charon.forwarding.interceptors.resilience;

import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorConfigurer;
import io.github.resilience4j.retry.RetryConfig;
import io.micrometer.core.instrument.MeterRegistry;

import static io.github.resilience4j.retry.RetryRegistry.of;

public class RetryerConfigurer extends RequestForwardingInterceptorConfigurer<Retryer> {

    private RetryerConfigurer() {
        super(new Retryer());
    }

    public static RetryerConfigurer retryer() {
        return new RetryerConfigurer();
    }

    public RetryerConfigurer configuration(RetryConfig.Builder retryConfigBuilder) {
        configuredObject.setRegistry(of(retryConfigBuilder.build()));
        return this;
    }

    public RetryerConfigurer meterRegistry(MeterRegistry meterRegistry) {
        configuredObject.setMeterRegistry(meterRegistry);
        return this;
    }
}
