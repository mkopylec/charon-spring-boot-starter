package com.github.mkopylec.charon.configuration;

public class TracingProperties {

    /**
     * Flag for enabling and disabling tracing HTTP requests proxying processes.
     */
    private boolean enabled = false;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
