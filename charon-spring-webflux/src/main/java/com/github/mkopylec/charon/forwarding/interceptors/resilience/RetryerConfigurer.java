package com.github.mkopylec.charon.forwarding.interceptors.resilience;

import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorConfigurer;
import io.github.resilience4j.retry.RetryConfig.Builder;
import io.micrometer.core.instrument.MeterRegistry;

import static io.github.resilience4j.retry.RetryRegistry.of;

public class RetryerConfigurer extends RequestForwardingInterceptorConfigurer<Retryer> {

    private RetryerConfigurer() {
        super(new Retryer());
    }

    public static RetryerConfigurer retryer() {
        return new RetryerConfigurer();
    }

    public RetryerConfigurer configuration(Builder<HttpResponse> retryConfiguration) {
        configuredObject.setRegistry(of(retryConfiguration.build()));
        return this;
    }

    public RetryerConfigurer meterRegistry(MeterRegistry meterRegistry) {
        configuredObject.setMeterRegistry(meterRegistry);
        return this;
    }
}
