package com.github.mkopylec.charon.configuration;

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
         * Metrics reporting interval in seconds.
         */
        private int intervalInSeconds = 60;
        /**
         * Properties responsible for reporting collected metrics to application logger.
         */
        private LoggerProperties logger = new LoggerProperties();
        /**
         * Properties responsible for reporting collected metrics to Graphite server.
         */
        private GraphiteProperties graphite = new GraphiteProperties();

        public int getIntervalInSeconds() {
            return intervalInSeconds;
        }

        public void setIntervalInSeconds(int intervalInSeconds) {
            this.intervalInSeconds = intervalInSeconds;
        }

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

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }
        }

        public static class GraphiteProperties {

            /**
             * Flag for enabling and disabling reporting metrics to Graphite server.
             */
            private boolean enabled = false;
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
