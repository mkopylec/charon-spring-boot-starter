package com.github.mkopylec.charon.forwarding;

import java.time.Duration;

import static java.lang.String.join;

public class Utils {

    public static int toMillis(Duration duration) {
        return (int) duration.toMillis();
    }

    public static String metricName(String... parts) {
        return "charon." + join(".", parts);
    }

    private Utils() {
    }
}
