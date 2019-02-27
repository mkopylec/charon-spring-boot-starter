package com.github.mkopylec.charon.core.http;

import com.github.mkopylec.charon.configuration.MappingProperties;

public class NoOpReceivedResponseInterceptor implements ReceivedResponseInterceptor {

    @Override
    public void intercept(ResponseData data, MappingProperties mapping) {

    }
}
