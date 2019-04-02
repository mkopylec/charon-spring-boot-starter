package com.github.mkopylec.charon.interceptors.latency;

import com.github.mkopylec.charon.interceptors.HttpRequest;
import com.github.mkopylec.charon.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.interceptors.HttpResponse;
import com.github.mkopylec.charon.interceptors.RequestForwardingInterceptor;
import io.micrometer.core.instrument.MeterRegistry;

import static org.springframework.util.Assert.notNull;

class LatencyMeter implements RequestForwardingInterceptor {

    private boolean enabled;
    private MeterRegistry meterRegistry;

    LatencyMeter(MeterRegistry meterRegistry) {
        enabled = true;
        this.meterRegistry = meterRegistry;
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        // TODO Implement latency meter
        return null;
    }

    @Override
    public void validate() {
        notNull(meterRegistry, "No meter registry set");
    }

    @Override
    public int getOrder() {
        return LATENCY_METER_ORDER;
    }

    void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
