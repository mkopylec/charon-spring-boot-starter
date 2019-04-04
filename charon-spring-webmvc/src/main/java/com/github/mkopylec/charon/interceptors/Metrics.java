package com.github.mkopylec.charon.interceptors;

import static java.lang.String.join;

public class Metrics {

    public static String metricName(String... parts) {
        return "charon." + join(".", parts);
    }

    private Metrics() {
    }
}
