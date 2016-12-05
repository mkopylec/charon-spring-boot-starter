package com.github.mkopylec.charon.configuration;

import org.springframework.boot.context.properties.NestedConfigurationProperty;

public class MetricsProperties {

    /**
     * Flag for enabling and disabling collecting metrics during HTTP requests forwarding.
     */
    private boolean enabled = false;
    /**
     * Global metrics names prefix.
     */
    private String namesPrefix = "charon";
    /**
     * Properties responsible for reporting collected metrics.
     */
    @NestedConfigurationProperty
    private ReportingProperties reporting = new ReportingProperties();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getNamesPrefix() {
        return namesPrefix;
    }

    public void setNamesPrefix(String namesPrefix) {
        this.namesPrefix = namesPrefix;
    }

    public ReportingProperties getReporting() {
        return reporting;
    }

    public void setReporting(ReportingProperties reporting) {
        this.reporting = reporting;
    }

    public static class ReportingProperties {

        /**
         * Properties responsible for reporting collected metrics to application logger.
         */
        @NestedConfigurationProperty
        private LoggerProperties logger = new LoggerProperties();
        /**
         * Properties responsible for reporting collected metrics to Graphite server.
         */
        @NestedConfigurationProperty
        private GraphiteProperties graphite = new GraphiteProperties();

        public LoggerProperties getLogger() {
            return logger;
        }

        public void setLogger(LoggerProperties logger) {
            this.logger = logger;
        }

        public GraphiteProperties getGraphite() {
            return graphite;
        }

        public void setGraphite(GraphiteProperties graphite) {
            this.graphite = graphite;
        }

        public static class LoggerProperties {

            /**
             * Flag for enabling and disabling reporting metrics to application logger.
             */
            private boolean enabled = false;
            /**
             * Metrics reporting interval in seconds.
             */
            private int intervalInSeconds = 60;

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public int getIntervalInSeconds() {
                return intervalInSeconds;
            }

            public void setIntervalInSeconds(int intervalInSeconds) {
                this.intervalInSeconds = intervalInSeconds;
            }
        }

        public static class GraphiteProperties {

            /**
             * Flag for enabling and disabling reporting metrics to Graphite server.
             */
            private boolean enabled = false;
            /**
             * Metrics reporting interval in seconds.
             */
            private int intervalInSeconds = 60;
            /**
             * Graphite server hostname.
             */
            private String hostname;
            /**
             * Graphite server port.
             */
            private int port = 2003;

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public int getIntervalInSeconds() {
                return intervalInSeconds;
            }

            public void setIntervalInSeconds(int intervalInSeconds) {
                this.intervalInSeconds = intervalInSeconds;
            }

            public String getHostname() {
                return hostname;
            }

            public void setHostname(String hostname) {
                this.hostname = hostname;
            }

            public int getPort() {
                return port;
            }

            public void setPort(int port) {
                this.port = port;
            }
        }
    }
}
