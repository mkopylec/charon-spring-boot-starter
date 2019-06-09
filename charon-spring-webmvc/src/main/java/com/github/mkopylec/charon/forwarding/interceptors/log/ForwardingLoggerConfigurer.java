package com.github.mkopylec.charon.forwarding.interceptors.log;

import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorConfigurer;

public class ForwardingLoggerConfigurer extends RequestForwardingInterceptorConfigurer<ForwardingLogger> {

    private ForwardingLoggerConfigurer() {
        super(new ForwardingLogger());
    }

    public static ForwardingLoggerConfigurer forwardingLogger() {
        return new ForwardingLoggerConfigurer();
    }

    public ForwardingLoggerConfigurer successLogLevel(LogLevel successLogLevel) {
        configuredObject.setSuccessLogLevel(successLogLevel);
        return this;
    }

    public ForwardingLoggerConfigurer clientErrorLogLevel(LogLevel clientErrorLogLevel) {
        configuredObject.setClientErrorLogLevel(clientErrorLogLevel);
        return this;
    }

    public ForwardingLoggerConfigurer serverErrorLogLevel(LogLevel serverErrorLogLevel) {
        configuredObject.setServerErrorLogLevel(serverErrorLogLevel);
        return this;
    }

    public ForwardingLoggerConfigurer unexpectedErrorLogLevel(LogLevel unexpectedErrorLogLevel) {
        configuredObject.setUnexpectedErrorLogLevel(unexpectedErrorLogLevel);
        return this;
    }
}
