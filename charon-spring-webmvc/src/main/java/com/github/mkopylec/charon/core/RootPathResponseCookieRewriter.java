package com.github.mkopylec.charon.core;

import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;

public class RootPathResponseCookieRewriter implements ResponseCookieRewriter {

    protected RootPathResponseCookieRewriter() {
    }

    @Override
    public ResponseCookie rewrite(HttpCookie incomingRequestCookie, ResponseCookie incomingResponseCookie) {
        // TODO Implement
        return null;
    }
}
