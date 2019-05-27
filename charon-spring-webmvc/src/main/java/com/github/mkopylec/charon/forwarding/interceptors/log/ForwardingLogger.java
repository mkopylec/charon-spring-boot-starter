package com.github.mkopylec.charon.forwarding.interceptors.log;

import java.net.URI;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;

import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.FORWARDING_LOGGER;
import static com.github.mkopylec.charon.forwarding.interceptors.log.LogLevel.DEBUG;
import static com.github.mkopylec.charon.forwarding.interceptors.log.LogLevel.ERROR;
import static com.github.mkopylec.charon.forwarding.interceptors.log.LogLevel.INFO;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.util.Assert.notNull;

class ForwardingLogger implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(ForwardingLogger.class);

    private LogLevel successLogLevel;
    private LogLevel clientErrorLogLevel;
    private LogLevel serverErrorLogLevel;
    private LogLevel unexpectedErrorLogLevel;

    ForwardingLogger() {
        successLogLevel = DEBUG;
        clientErrorLogLevel = INFO;
        serverErrorLogLevel = ERROR;
        unexpectedErrorLogLevel = ERROR;
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        String oldMethod = request.getMethodValue();
        URI oldUri = request.getURI();
        String forwardingName = execution.getMappingName();
        try {
            HttpResponse response = execution.execute(request);
            String logMessage = "Forwarding: {} {} -> '{}' -> {} {} {}";
            if (response.getStatusCode().is5xxServerError()) {
                log(serverErrorLogLevel, logMessage, oldMethod, oldUri, forwardingName, request.getMethodValue(), request.getURI(), response.getRawStatusCode());
            } else if (response.getStatusCode().is4xxClientError()) {
                log(clientErrorLogLevel, logMessage, oldMethod, oldUri, forwardingName, request.getMethodValue(), request.getURI(), response.getRawStatusCode());
            } else {
                log(successLogLevel, logMessage, oldMethod, oldUri, forwardingName, request.getMethodValue(), request.getURI(), response.getRawStatusCode());
            }
            return response;
        } catch (RuntimeException e) {
            log(unexpectedErrorLogLevel, "Forwarding: {} {} -> '{}' -> {}", oldMethod, oldUri, forwardingName, e.getMessage());
            throw e;
        }
    }

    @Override
    public void validate() {
        notNull(successLogLevel, "No success log level set");
        notNull(clientErrorLogLevel, "No client error log level set");
        notNull(serverErrorLogLevel, "No server error log level set");
        notNull(unexpectedErrorLogLevel, "No unexpected error log level set");
    }

    @Override
    public int getOrder() {
        return FORWARDING_LOGGER.getOrder();
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

    private void log(LogLevel level, String message, Object... parameters) {
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
