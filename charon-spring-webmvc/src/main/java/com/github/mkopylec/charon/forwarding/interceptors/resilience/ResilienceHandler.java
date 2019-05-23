package com.github.mkopylec.charon.forwarding.interceptors.resilience;

import java.util.function.Function;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;

public abstract class ResilienceHandler<R> implements RequestForwardingInterceptor {

    private boolean enabled;
    protected R registry;
    private MeterBinder metrics;
    private MeterRegistry meterRegistry;

    protected ResilienceHandler(R registry) {
        enabled = true;
        this.registry = registry;
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        if (!enabled) {
            return execution.execute(request);
        }
        return forwardRequest(request, execution);
    }

    protected abstract HttpResponse forwardRequest(HttpRequest request, HttpRequestExecution execution);

    protected void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    protected void setRegistry(R registry) {
        this.registry = registry;
    }

    protected void setMeterRegistry(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    protected void setupMetrics(Function<R, ? extends MeterBinder> metricsCreator) {
        if (meterRegistry == null) {
            return;
        }
        if (metrics == null) {
            metrics = metricsCreator.apply(registry);
            metrics.bindTo(meterRegistry);
        }
    }
}
