package com.github.mkopylec.charon.configuration;

import java.time.Duration;

public class TimeoutConfigurer {

    private TimeoutConfiguration timeoutConfiguration;

    private TimeoutConfigurer() {
        timeoutConfiguration = new TimeoutConfiguration();
    }

    public static TimeoutConfigurer timeout() {
        return new TimeoutConfigurer();
    }

    public TimeoutConfigurer connection(Duration connection) {
        timeoutConfiguration.setConnection(connection);
        return this;
    }

    public TimeoutConfigurer read(Duration read) {
        timeoutConfiguration.setRead(read);
        return this;
    }

    public TimeoutConfigurer write(Duration write) {
        timeoutConfiguration.setWrite(write);
        return this;
    }

    TimeoutConfiguration getConfiguration() {
        return timeoutConfiguration;
    }
}
