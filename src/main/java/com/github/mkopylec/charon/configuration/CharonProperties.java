package com.github.mkopylec.charon.configuration;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.springframework.boot.context.properties.ConfigurationProperties;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.builder.ToStringStyle.NO_CLASS_NAME_STYLE;
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
     * Properties responsible for timeouts while forwarding HTTP requests.
     */
    private Timeout timeout = new Timeout();
    /**
     * Properties responsible for retrying of HTTP requests forwarding.
     */
    private Retrying retrying = new Retrying();
    /**
     * Properties responsible for collecting metrics during HTTP requests forwarding.
     */
    private Metrics metrics = new Metrics();
    /**
     * Properties responsible for proxy mappings updates.
     */
    private MappingsUpdate mappingsUpdate = new MappingsUpdate();
    /**
     * List of proxy mappings.
     */
    private List<Mapping> mappings = new ArrayList<>();

    public int getFilterOrder() {
        return filterOrder;
    }

    public void setFilterOrder(int filterOrder) {
        this.filterOrder = filterOrder;
    }

    public Timeout getTimeout() {
        return timeout;
    }

    public void setTimeout(Timeout timeout) {
        this.timeout = timeout;
    }

    public Retrying getRetrying() {
        return retrying;
    }

    public void setRetrying(Retrying retrying) {
        this.retrying = retrying;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public void setMetrics(Metrics metrics) {
        this.metrics = metrics;
    }

    public MappingsUpdate getMappingsUpdate() {
        return mappingsUpdate;
    }

    public void setMappingsUpdate(MappingsUpdate mappingsUpdate) {
        this.mappingsUpdate = mappingsUpdate;
    }

    public List<Mapping> getMappings() {
        return mappings;
    }

    public void setMappings(List<Mapping> mappings) {
        this.mappings = mappings;
    }

    public static class Timeout {

        /**
         * Connect timeout for HTTP requests forwarding.
         */
        private int connect = 500;
        /**
         * Read timeout for HTTP requests forwarding.
         */
        private int read = 2000;

        public int getConnect() {
            return connect;
        }

        public void setConnect(int connect) {
            this.connect = connect;
        }

        public int getRead() {
            return read;
        }

        public void setRead(int read) {
            this.read = read;
        }
    }

    public static class Retrying {

        /**
         * Maximum number of HTTP request forward tries.
         */
        private int maxAttempts = 3;

        public int getMaxAttempts() {
            return maxAttempts;
        }

        public void setMaxAttempts(int maxAttempts) {
            this.maxAttempts = maxAttempts;
        }
    }

    public static class Metrics {

        /**
         * Flag for enabling and disabling collecting metrics during HTTP requests forwarding.
         */
        private boolean enabled = false;
        /**
         * Flag for enabling and disabling reporting metrics via application logger.
         */
        private boolean loggingReporterEnabled = false;
        /**
         * Metrics reporting via logger interval in seconds.
         */
        private int loggingReporterReportingIntervalInSeconds = 60;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isLoggingReporterEnabled() {
            return loggingReporterEnabled;
        }

        public void setLoggingReporterEnabled(boolean loggingReporterEnabled) {
            this.loggingReporterEnabled = loggingReporterEnabled;
        }

        public int getLoggingReporterReportingIntervalInSeconds() {
            return loggingReporterReportingIntervalInSeconds;
        }

        public void setLoggingReporterReportingIntervalInSeconds(int loggingReporterReportingIntervalInSeconds) {
            this.loggingReporterReportingIntervalInSeconds = loggingReporterReportingIntervalInSeconds;
        }
    }

    public static class MappingsUpdate {

        /**
         * Flag for enabling and disabling mappings updates.
         */
        private boolean enabled = false;
        /**
         * Flag for enabling and disabling triggering mappings updates on non-HTTP errors occurred during HTTP requests forwarding.
         */
        private boolean onNonHttpError = true;
        /**
         * Interval in milliseconds between automatic mappings updates.
         */
        private int intervalInMillis = 30000;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isOnNonHttpError() {
            return onNonHttpError;
        }

        public void setOnNonHttpError(boolean onNonHttpError) {
            this.onNonHttpError = onNonHttpError;
        }

        public int getIntervalInMillis() {
            return intervalInMillis;
        }

        public void setIntervalInMillis(int intervalInMillis) {
            this.intervalInMillis = intervalInMillis;
        }
    }

    public static class Mapping {

        public static final String DEFAULT_METRICS_NAME = "charon";

        /**
         * Mapping metrics name.
         */
        private String metricsName = DEFAULT_METRICS_NAME;
        /**
         * Path for mapping incoming HTTP requests URIs.
         */
        private String path = "/";
        /**
         * List of destination hosts where HTTp requests will be forwarded.
         */
        private List<String> destinations = new ArrayList<>();
        /**
         * Flag for enabling and disabling mapped path stripping from forwarded request URI.
         */
        private boolean stripPath = true;

        public String getMetricsName() {
            return isNotBlank(metricsName) ? metricsName : DEFAULT_METRICS_NAME;
        }

        public void setMetricsName(String metricsName) {
            this.metricsName = metricsName;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public List<String> getDestinations() {
            return destinations;
        }

        public void setDestinations(List<String> destinations) {
            this.destinations = destinations;
        }

        public boolean isStripPath() {
            return stripPath;
        }

        public void setStripPath(boolean stripPath) {
            this.stripPath = stripPath;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, NO_CLASS_NAME_STYLE)
                    .append("path", path)
                    .append("destinations", destinations)
                    .append("stripPath", stripPath)
                    .toString();
        }
    }
}
