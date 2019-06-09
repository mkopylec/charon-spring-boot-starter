package com.github.mkopylec.charon.forwarding;

import java.time.Duration;

import com.github.mkopylec.charon.configuration.Valid;

import static java.time.Duration.ofMillis;
import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

public class TimeoutConfiguration implements Valid {

    private Duration connection;
    private Duration read;
    private Duration write;

    TimeoutConfiguration() {
        connection = ofMillis(100);
        read = ofMillis(1000);
        write = ofMillis(500);
    }

    @Override
    public void validate() {
        notNull(connection, "No connection timeout set");
        isTrue(!connection.isNegative(), "Invalid connection timeout value: " + connection.toMillis() + " ms");
        notNull(read, "No read timeout set");
        isTrue(!read.isNegative(), "Invalid read timeout value: " + read.toMillis() + " ms");
        notNull(write, "No write timeout set");
        isTrue(!write.isNegative(), "Invalid write timeout value: " + write.toMillis() + " ms");
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
