package com.github.mkopylec.charon.forwarding;

import java.time.Duration;

import org.springframework.http.HttpHeaders;

import static java.lang.String.join;

public class Utils {

    public static int toMillis(Duration duration) {
        return (int) duration.toMillis();
    }

    public static String metricName(String... parts) {
        return "charon." + join(".", parts);
    }

    public static HttpHeaders copyHeaders(HttpHeaders headers) {
        HttpHeaders copy = new HttpHeaders();
        copy.putAll(headers);
        return copy;
    }

    private Utils() {
    }
}
