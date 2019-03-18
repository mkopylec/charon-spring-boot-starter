package com.github.mkopylec.charon.core;

import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;

public interface ResponseCookieRewriter {

    ResponseCookie rewrite(HttpCookie incomingRequestCookie, ResponseCookie incomingResponseCookie);
}
