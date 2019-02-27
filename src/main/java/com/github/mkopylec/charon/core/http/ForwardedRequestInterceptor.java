package com.github.mkopylec.charon.core.http;

import com.github.mkopylec.charon.configuration.MappingProperties;

public interface ForwardedRequestInterceptor {

    void intercept(RequestData data, MappingProperties mapping);
}
