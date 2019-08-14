package com.github.mkopylec.charon.configuration;

import java.time.Duration;
import java.util.function.Supplier;

import static java.time.Duration.ofMinutes;
import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

public class DynamicCharonConfiguration implements Valid {

    private Supplier<CharonConfiguration> update;
    private Duration updateRate;

    DynamicCharonConfiguration() {
        updateRate = ofMinutes(1);
    }

    public Supplier<CharonConfiguration> getUpdate() {
        return update;
    }

    void setUpdate(Supplier<CharonConfiguration> update) {
        this.update = update;
    }

    public Duration getUpdateRate() {
        return updateRate;
    }

    void setUpdateRate(Duration updateRate) {
        this.updateRate = updateRate;
    }

    @Override
    public void validate() {
        notNull(update, "No update set");
        notNull(updateRate, "No update rate set");
        isTrue(!updateRate.isNegative(), "Invalid update rate set");
    }
}
