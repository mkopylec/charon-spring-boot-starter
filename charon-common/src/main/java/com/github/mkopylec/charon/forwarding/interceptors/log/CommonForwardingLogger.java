package com.github.mkopylec.charon.forwarding.interceptors.log;

import java.net.URI;

import com.github.mkopylec.charon.configuration.Valid;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType;
import org.slf4j.Logger;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;

import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.FORWARDING_LOGGER;
import static com.github.mkopylec.charon.forwarding.interceptors.log.LogLevel.DEBUG;
import static com.github.mkopylec.charon.forwarding.interceptors.log.LogLevel.ERROR;
import static com.github.mkopylec.charon.forwarding.interceptors.log.LogLevel.INFO;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCause;
import static org.springframework.util.Assert.notNull;

abstract class CommonForwardingLogger implements Valid {

    private Logger log;
    private LogLevel successLogLevel;
    private LogLevel clientErrorLogLevel;
    private LogLevel serverErrorLogLevel;
    private LogLevel unexpectedErrorLogLevel;

    CommonForwardingLogger(Logger log) {
        this.log = log;
        successLogLevel = DEBUG;
        clientErrorLogLevel = INFO;
        serverErrorLogLevel = ERROR;
        unexpectedErrorLogLevel = ERROR;
    }

    @Override
    public void validate() {
        notNull(successLogLevel, "No success log level set");
        notNull(clientErrorLogLevel, "No client error log level set");
        notNull(serverErrorLogLevel, "No server error log level set");
        notNull(unexpectedErrorLogLevel, "No unexpected error log level set");
    }

    public RequestForwardingInterceptorType getType() {
        return FORWARDING_LOGGER;
    }

    void setSuccessLogLevel(LogLevel successLogLevel) {
        this.successLogLevel = successLogLevel;
    }

    void setClientErrorLogLevel(LogLevel clientErrorLogLevel) {
        this.clientErrorLogLevel = clientErrorLogLevel;
    }

    void setServerErrorLogLevel(LogLevel serverErrorLogLevel) {
        this.serverErrorLogLevel = serverErrorLogLevel;
    }

    void setUnexpectedErrorLogLevel(LogLevel unexpectedErrorLogLevel) {
        this.unexpectedErrorLogLevel = unexpectedErrorLogLevel;
    }

    void logForwardingResult(HttpStatusCode responseStatus, HttpMethod originalRequestMethod, HttpMethod forwardedRequestMethod, URI originalRequestUri, URI forwardedRequestUri, String mappingName) {
        String logMessage = "Forwarding: {} {} -> '{}' -> {} {} {}";
        if (responseStatus.is5xxServerError()) {
            log(serverErrorLogLevel, logMessage, originalRequestMethod, originalRequestUri, mappingName, forwardedRequestMethod, forwardedRequestUri, responseStatus.value());
        } else if (responseStatus.is4xxClientError()) {
            log(clientErrorLogLevel, logMessage, originalRequestMethod, originalRequestUri, mappingName, forwardedRequestMethod, forwardedRequestUri, responseStatus.value());
        } else {
            log(successLogLevel, logMessage, originalRequestMethod, originalRequestUri, mappingName, forwardedRequestMethod, forwardedRequestUri, responseStatus.value());
        }
    }

    void logForwardingError(RuntimeException e, HttpMethod originalRequestMethod, URI originalRequestUri, String mappingName) {
        Throwable cause = getRootCause(e);
        log(unexpectedErrorLogLevel, "Forwarding: {} {} -> '{}' -> {}: {}", originalRequestMethod, originalRequestUri, mappingName, cause.getClass().getName(), cause.getMessage());
    }

    private void log(LogLevel level, String message, Object... parameters) {
        switch (level) {
            case ERROR -> log.error(message, parameters);
            case WARN -> log.warn(message, parameters);
            case INFO -> log.info(message, parameters);
            case DEBUG -> log.debug(message, parameters);
            case TRACE -> log.trace(message, parameters);
        }
    }
}
