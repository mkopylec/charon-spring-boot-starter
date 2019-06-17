package com.github.mkopylec.charon.forwarding.interceptors.metrics;

import com.github.mkopylec.charon.configuration.Valid;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;

import static org.springframework.util.Assert.notNull;

abstract class BasicMeter implements Valid {

    private Logger log;
    private MeterRegistry meterRegistry;

    BasicMeter(Logger log) {
        this.log = log;
    }

    @Override
    public void validate() {
        notNull(meterRegistry, "No meter registry set");
    }

    Logger getLog() {
        return log;
    }

    MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }

    void setMeterRegistry(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }
}
