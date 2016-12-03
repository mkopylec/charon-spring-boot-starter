package com.github.mkopylec.charon.configuration;

import com.github.mkopylec.charon.exceptions.CharonException;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isBlank;
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
    @NestedConfigurationProperty
    private Timeout timeout = new Timeout();
    /**
     * Properties responsible for retrying of HTTP requests forwarding.
     */
    @NestedConfigurationProperty
    private Retrying retrying = new Retrying();
    /**
     * Properties responsible for collecting metrics during HTTP requests forwarding.
     */
    @NestedConfigurationProperty
    private Metrics metrics = new Metrics();
    /**
     * Properties responsible for tracing HTTP requests proxying processes.
     */
    @NestedConfigurationProperty
    private Tracing tracing = new Tracing();
    /**
     * Properties responsible for asynchronous HTTP requests forwarding.
     */
    @NestedConfigurationProperty
    private AsynchronousForwardingThreadPool asynchronousForwardingThreadPool = new AsynchronousForwardingThreadPool();
    /**
     * List of proxy mappings.
     */
    @NestedConfigurationProperty
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

    public Tracing getTracing() {
        return tracing;
    }

    public void setTracing(Tracing tracing) {
        this.tracing = tracing;
    }

    public AsynchronousForwardingThreadPool getAsynchronousForwardingThreadPool() {
        return asynchronousForwardingThreadPool;
    }

    public void setAsynchronousForwardingThreadPool(AsynchronousForwardingThreadPool asynchronousForwardingThreadPool) {
        this.asynchronousForwardingThreadPool = asynchronousForwardingThreadPool;
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

        public static final String MAPPING_NAME_RETRY_ATTRIBUTE = "mapping-name";

        /**
         * Maximum number of HTTP request forward tries.
         */
        private int maxAttempts = 3;
        /**
         * Properties responsible for triggering HTTP requests forward retries.
         */
        @NestedConfigurationProperty
        private RetryOn retryOn = new RetryOn();

        public int getMaxAttempts() {
            return maxAttempts;
        }

        public void setMaxAttempts(int maxAttempts) {
            this.maxAttempts = maxAttempts;
        }

        public RetryOn getRetryOn() {
            return retryOn;
        }

        public void setRetryOn(RetryOn retryOn) {
            this.retryOn = retryOn;
        }

        public static class RetryOn {

            /**
             * Flag for enabling and disabling triggering HTTP requests forward retries on 4xx HTTP responses from destination.
             */
            private boolean clientHttpError = false;
            /**
             * Flag for enabling and disabling triggering HTTP requests forward retries on 5xx HTTP responses from destination.
             */
            private boolean serverHttpError = true;
            /**
             * Comma-separated list of exceptions that triggers HTTP request forward retries.
             */
            private String exceptions = "java.lang.Exception";
            private List<Class<? extends Throwable>> retryableExceptions = new ArrayList<>();

            public boolean isClientHttpError() {
                return clientHttpError;
            }

            public void setClientHttpError(boolean clientHttpError) {
                this.clientHttpError = clientHttpError;
            }

            public boolean isServerHttpError() {
                return serverHttpError;
            }

            public void setServerHttpError(boolean serverHttpError) {
                this.serverHttpError = serverHttpError;
            }

            @SuppressWarnings("unchecked")
            public List<Class<? extends Throwable>> getExceptions() {
                if (isBlank(exceptions)) {
                    return emptyList();
                }
                if (retryableExceptions.isEmpty()) {
                    retryableExceptions = Stream.of(exceptions.split(","))
                            .map(exceptionType -> {
                                try {
                                    return (Class<? extends Throwable>) Class.forName(exceptionType.trim());
                                } catch (ClassNotFoundException e) {
                                    throw new CharonException("Invalid retryable exception: " + exceptionType, e);
                                }
                            })
                            .collect(toList());
                    if (clientHttpError) {
                        retryableExceptions.add(HttpClientErrorException.class);
                    }
                    if (serverHttpError) {
                        retryableExceptions.add(HttpServerErrorException.class);
                    }
                }
                return retryableExceptions;
            }

            public void setExceptions(String exceptions) {
                this.exceptions = exceptions;
            }
        }
    }

    public static class Metrics {

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
        private Reporting reporting = new Reporting();

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

        public Reporting getReporting() {
            return reporting;
        }

        public void setReporting(Reporting reporting) {
            this.reporting = reporting;
        }

        public static class Reporting {

            /**
             * Properties responsible for reporting collected metrics to application logger.
             */
            @NestedConfigurationProperty
            private Logger logger = new Logger();
            /**
             * Properties responsible for reporting collected metrics to Graphite server.
             */
            @NestedConfigurationProperty
            private Graphite graphite = new Graphite();

            public Logger getLogger() {
                return logger;
            }

            public void setLogger(Logger logger) {
                this.logger = logger;
            }

            public Graphite getGraphite() {
                return graphite;
            }

            public void setGraphite(Graphite graphite) {
                this.graphite = graphite;
            }

            public static class Logger {

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

            public static class Graphite {

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

    public static class Tracing {

        /**
         * Flag for enabling and disabling tracing HTTP requests proxying processes.
         */
        private boolean enabled = false;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    public static class AsynchronousForwardingThreadPool {

        /**
         * Thread pool executor queue capacity.
         */
        private int queueCapacity = 50;
        /**
         * Properties responsible for number of threads in thread pool executor.
         */
        @NestedConfigurationProperty
        private Size size = new Size();

        public int getQueueCapacity() {
            return queueCapacity;
        }

        public void setQueueCapacity(int queueCapacity) {
            this.queueCapacity = queueCapacity;
        }

        public Size getSize() {
            return size;
        }

        public void setSize(Size size) {
            this.size = size;
        }

        public static class Size {

            /**
             * Thread pool executor initial number of threads.
             */
            private int initial = 5;
            /**
             * Thread pool executor maximum number of threads.
             */
            private int maximum = 30;

            public int getInitial() {
                return initial;
            }

            public void setInitial(int initial) {
                this.initial = initial;
            }

            public int getMaximum() {
                return maximum;
            }

            public void setMaximum(int maximum) {
                this.maximum = maximum;
            }
        }
    }

    public static class Mapping {

        /**
         * Name of the mapping.
         */
        private String name;
        /**
         * Path for mapping incoming HTTP requests URIs.
         */
        private String path = "/";
        /**
         * List of destination hosts where HTTP requests will be forwarded.
         */
        private List<String> destinations = new ArrayList<>();
        /**
         * Flag for enabling and disabling asynchronous HTTP request forwarding.
         */
        private boolean asynchronous = false;
        /**
         * Flag for enabling and disabling mapped path stripping from forwarded request URI.
         */
        private boolean stripPath = true;
        /**
         * Flag for enabling and disabling retrying of HTTP requests forwarding.
         */
        private boolean retryable = false;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public boolean isAsynchronous() {
            return asynchronous;
        }

        public void setAsynchronous(boolean asynchronous) {
            this.asynchronous = asynchronous;
        }

        public boolean isStripPath() {
            return stripPath;
        }

        public void setStripPath(boolean stripPath) {
            this.stripPath = stripPath;
        }

        public boolean isRetryable() {
            return retryable;
        }

        public void setRetryable(boolean retryable) {
            this.retryable = retryable;
        }

        public Mapping copy() {
            Mapping clone = new Mapping();
            clone.setName(name);
            clone.setPath(path);
            clone.setDestinations(destinations == null ? null : new ArrayList<>(destinations));
            clone.setAsynchronous(asynchronous);
            clone.setStripPath(stripPath);
            clone.setRetryable(retryable);
            return clone;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, NO_CLASS_NAME_STYLE)
                    .append("name", name)
                    .append("path", path)
                    .append("destinations", destinations)
                    .append("asynchronous", asynchronous)
                    .append("stripPath", stripPath)
                    .append("retryable", retryable)
                    .toString();
        }
    }
}
