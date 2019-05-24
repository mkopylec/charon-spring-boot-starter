package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import org.springframework.http.HttpHeaders;

class HeadersUtils {

    static HttpHeaders copyHeaders(HttpHeaders headers) {
        HttpHeaders copy = new HttpHeaders();
        copy.putAll(headers);
        return copy;
    }

    private HeadersUtils() {
    }
}
