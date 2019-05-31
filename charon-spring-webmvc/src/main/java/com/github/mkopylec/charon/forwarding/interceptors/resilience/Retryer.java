package com.github.mkopylec.charon.forwarding.interceptors.resilience;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import io.github.resilience4j.micrometer.tagged.TaggedRetryMetrics;
import io.github.resilience4j.micrometer.tagged.TaggedRetryMetrics.MetricNames;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.slf4j.Logger;

import static com.github.mkopylec.charon.forwarding.interceptors.MetricsUtils.metricName;
import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.RETRYING_HANDLER;
import static io.github.resilience4j.micrometer.tagged.TaggedRetryMetrics.ofRetryRegistry;
import static io.github.resilience4j.retry.RetryRegistry.of;
import static java.time.Duration.ofMillis;
import static org.slf4j.LoggerFactory.getLogger;

class Retryer extends ResilienceHandler<RetryRegistry> {

    private static final String RETRYING_METRICS_NAME = "retrying";

    private static final Logger log = getLogger(Retryer.class);

    Retryer() {
        super(of(RetryConfig.<HttpResponse>custom()
                .waitDuration(ofMillis(10))
                .retryOnResult(result -> result.getStatusCode().is5xxServerError())
                .build()));
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        log.trace("[Start] Retrying of '{}' request mapping", execution.getMappingName());
        Retry retry = registry.retry(execution.getMappingName());
        setupMetrics(registry -> createMetrics(registry, execution.getMappingName()));
        HttpResponse response = retry.executeSupplier(() -> execution.execute(request));
        log.trace("[End] Retrying of '{}' request mapping", execution.getMappingName());
        return response;
    }

    @Override
    public int getOrder() {
        return RETRYING_HANDLER.getOrder();
    }

    private TaggedRetryMetrics createMetrics(RetryRegistry registry, String mappingName) {
        String callsMetricName = metricName(mappingName, RETRYING_METRICS_NAME, "calls");
        MetricNames metricNames = MetricNames.custom()
                .callsMetricName(callsMetricName)
                .build();
        return ofRetryRegistry(metricNames, registry);
    }
}
