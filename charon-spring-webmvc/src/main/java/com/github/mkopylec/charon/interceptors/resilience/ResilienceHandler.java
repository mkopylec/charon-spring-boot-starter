package com.github.mkopylec.charon.interceptors.resilience;

import com.github.mkopylec.charon.interceptors.RequestForwardingInterceptor;

public abstract class ResilienceHandler<C> implements RequestForwardingInterceptor {

    protected boolean enabled;
    protected C configuration;
    protected boolean measured;

    protected ResilienceHandler(C configuration) {
        enabled = true;
        this.configuration = configuration;
    }

    protected void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    protected void setConfiguration(C configuration) {
        this.configuration = configuration;
    }

    protected void setMeasured(boolean measured) {
        this.measured = measured;
    }
}
