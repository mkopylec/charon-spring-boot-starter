package com.github.mkopylec.charon.forwarding;

import java.time.Duration;

import com.github.mkopylec.charon.configuration.Configurer;

public class TimeoutConfigurer extends Configurer<TimeoutConfiguration> {

    private TimeoutConfigurer() {
        super(new TimeoutConfiguration());
    }

    public static TimeoutConfigurer timeout() {
        return new TimeoutConfigurer();
    }

    public TimeoutConfigurer connection(Duration connection) {
        configuredObject.setConnection(connection);
        return this;
    }

    public TimeoutConfigurer read(Duration read) {
        configuredObject.setRead(read);
        return this;
    }

    public TimeoutConfigurer write(Duration write) {
        configuredObject.setWrite(write);
        return this;
    }

    @Override
    protected TimeoutConfiguration configure() {
        return super.configure();
    }
}
