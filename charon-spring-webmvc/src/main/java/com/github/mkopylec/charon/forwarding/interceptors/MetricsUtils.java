package com.github.mkopylec.charon.forwarding.interceptors;

import static java.lang.String.join;

public class MetricsUtils {

    public static String metricName(String... parts) {
        return "charon." + join(".", parts);
    }

    private MetricsUtils() {
    }
}
