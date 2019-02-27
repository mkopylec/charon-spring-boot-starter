package com.github.mkopylec.charon.core.http;

import com.github.mkopylec.charon.configuration.MappingProperties;

public class NoOpForwardedRequestInterceptor implements ForwardedRequestInterceptor {

    @Override
    public void intercept(RequestData data, MappingProperties mapping) {

    }
}
