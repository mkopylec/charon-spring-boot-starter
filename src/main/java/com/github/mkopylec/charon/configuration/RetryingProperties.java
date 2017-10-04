package com.github.mkopylec.charon.configuration;

import com.github.mkopylec.charon.exceptions.CharonException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class RetryingProperties {

    public static final String MAPPING_NAME_RETRY_ATTRIBUTE = "mapping-name";

    /**
     * Maximum number of HTTP request forward tries.
     */
    private int maxAttempts = 3;
    /**
     * Properties responsible for triggering HTTP requests forward retries.
     */
    private RetryOnProperties retryOn = new RetryOnProperties();

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public RetryOnProperties getRetryOn() {
        return retryOn;
    }

    public void setRetryOn(RetryOnProperties retryOn) {
        this.retryOn = retryOn;
    }

    public static class RetryOnProperties {

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
