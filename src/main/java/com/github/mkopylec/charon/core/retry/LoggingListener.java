package com.github.mkopylec.charon.core.retry;

import com.github.mkopylec.charon.core.trace.TraceInterceptor;
import org.slf4j.Logger;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.listener.RetryListenerSupport;

import static com.github.mkopylec.charon.configuration.CharonProperties.Retrying.MAPPING_NAME_RETRY_ATTRIBUTE;
import static org.slf4j.LoggerFactory.getLogger;

public class LoggingListener extends RetryListenerSupport {

    private static final Logger log = getLogger(LoggingListener.class);

    protected final TraceInterceptor traceInterceptor;

    public LoggingListener(TraceInterceptor traceInterceptor) {
        this.traceInterceptor = traceInterceptor;
    }

    @Override
    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        Object mappingName = context.getAttribute(MAPPING_NAME_RETRY_ATTRIBUTE);
        if (mappingName != null) {
            log.debug("Attempt {} to forward HTTP request using '{}' mapping has failed. {}", context.getRetryCount() + 1, mappingName, throwable.getMessage());
            traceInterceptor.onForwardError(throwable);
        }
    }

    @Override
    public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        Object mappingName = context.getAttribute(MAPPING_NAME_RETRY_ATTRIBUTE);
        if (mappingName != null) {
            if (throwable != null) {
                log.error("All {} attempts to forward HTTP request using '{}' mapping has failed", context.getRetryCount(), mappingName);
            } else {
                log.debug("Attempt {} to forward HTTP request using '{}' mapping has succeeded", context.getRetryCount() + 1, mappingName);
            }
        }
    }
}
