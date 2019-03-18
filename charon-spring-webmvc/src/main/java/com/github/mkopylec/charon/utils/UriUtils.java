package com.github.mkopylec.charon.utils;

import java.net.URI;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class UriUtils {

    public static List<URI> toUris(String... uris) {
        return toUris(asList(uris));
    }

    public static List<URI> toUris(List<String> uris) {
        return uris.stream()
                .map(URI::create)
                .collect(toList());
    }

    private UriUtils() {
    }
}
