package com.github.mkopylec.charon.forwarding.interceptors.log;

import com.github.mkopylec.charon.configuration.Valid;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType;
import org.slf4j.Logger;

import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.FORWARDING_LOGGER;
import static com.github.mkopylec.charon.forwarding.interceptors.log.LogLevel.DEBUG;
import static com.github.mkopylec.charon.forwarding.interceptors.log.LogLevel.ERROR;
import static com.github.mkopylec.charon.forwarding.interceptors.log.LogLevel.INFO;
import static org.springframework.util.Assert.notNull;

abstract class BasicForwardingLogger implements Valid {

    private Logger log;
    LogLevel successLogLevel;
    LogLevel clientErrorLogLevel;
    LogLevel serverErrorLogLevel;
    LogLevel unexpectedErrorLogLevel;

    BasicForwardingLogger(Logger log) {
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

    void log(LogLevel level, String message, Object... parameters) {
        switch (level) {
            case ERROR:
                log.error(message, parameters);
                break;
            case WARN:
                log.warn(message, parameters);
                break;
            case INFO:
                log.info(message, parameters);
                break;
            case DEBUG:
                log.debug(message, parameters);
                break;
            case TRACE:
                log.trace(message, parameters);
                break;
        }
    }
}
