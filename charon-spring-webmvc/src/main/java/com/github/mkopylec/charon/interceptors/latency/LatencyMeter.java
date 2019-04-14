package com.github.mkopylec.charon.interceptors.latency;

import java.time.Duration;

import com.github.mkopylec.charon.interceptors.HttpRequest;
import com.github.mkopylec.charon.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.interceptors.HttpResponse;
import com.github.mkopylec.charon.interceptors.RequestForwardingInterceptor;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;

import static com.github.mkopylec.charon.interceptors.MetricsUtils.metricName;
import static java.lang.System.nanoTime;
import static java.time.Duration.ofNanos;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.util.Assert.notNull;

class LatencyMeter implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(LatencyMeter.class);

    private boolean enabled;
    private MeterRegistry meterRegistry;

    LatencyMeter(MeterRegistry meterRegistry) {
        enabled = true;
        this.meterRegistry = meterRegistry;
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        if (!enabled) {
            return execution.execute(request);
        }
        log.trace("[Start] Collect metrics of '{}' request mapping", execution.getMappingName());
        long startingTime = nanoTime();
        try {
            return execution.execute(request);
        } finally {
            captureLatencyMetric(execution.getMappingName(), startingTime);
            log.trace("[End] Collect metrics of '{}' request mapping", execution.getMappingName());
        }
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

    private void captureLatencyMetric(String forwardingName, long startingTime) {
        String metricName = metricName(forwardingName, "latency"); // TODO If timers collect more than only latency the class and metric names must be changed
        Duration responseTime = ofNanos(nanoTime() - startingTime);
        meterRegistry.timer(metricName).record(responseTime);
    }
}
