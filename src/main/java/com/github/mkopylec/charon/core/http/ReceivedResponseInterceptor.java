package com.github.mkopylec.charon.core.http;

import com.github.mkopylec.charon.configuration.MappingProperties;

public interface ReceivedResponseInterceptor {

    void intercept(ResponseData data, MappingProperties mapping);
}
