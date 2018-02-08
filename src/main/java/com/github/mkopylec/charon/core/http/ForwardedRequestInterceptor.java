package com.github.mkopylec.charon.core.http;

public interface ForwardedRequestInterceptor {

    void intercept(RequestData data);
}
