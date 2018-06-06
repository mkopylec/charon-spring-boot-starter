package com.github.mkopylec.charon.configuration;

public class MetricsProperties {

    /**
     * Global metrics names prefix.
     */
    private String namesPrefix = "charon";

    public String getNamesPrefix() {
        return namesPrefix;
    }

    public void setNamesPrefix(String namesPrefix) {
        this.namesPrefix = namesPrefix;
    }
}
