package com.github.mkopylec.charon.configuration;

import java.time.Duration;

import static java.time.Duration.ofMillis;

public class TimeoutConfiguration {

    private Duration connection;
    private Duration read;
    private Duration write;

    TimeoutConfiguration() {
        connection = ofMillis(200);
        read = ofMillis(1000);
        write = ofMillis(1000);
    }

    public Duration getConnection() {
        return connection;
    }

    void setConnection(Duration connection) {
        this.connection = connection;
    }

    public Duration getRead() {
        return read;
    }

    void setRead(Duration read) {
        this.read = read;
    }

    public Duration getWrite() {
        return write;
    }

    void setWrite(Duration write) {
        this.write = write;
    }
}
