package com.github.mkopylec.charon.application;

import com.github.mkopylec.charon.core.http.ForwardedRequestInterceptor;
import com.github.mkopylec.charon.core.http.ReceivedResponseInterceptor;
import com.github.mkopylec.charon.core.http.RequestData;
import com.github.mkopylec.charon.core.http.ResponseData;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpStatus.CREATED;

@Component
@ConditionalOnProperty("test.interceptors-enabled")
public class TestForwardedRequestInterceptor implements ForwardedRequestInterceptor, ReceivedResponseInterceptor {

    public static final String INTERCEPTED_AUTHORIZATION = "intercepted-authorization";
    public static final String INTERCEPTED_BODY = "intercepted-body";

    @Override
    public void intercept(RequestData data) {
        data.setMethod(DELETE);
        data.getHeaders().set(AUTHORIZATION, INTERCEPTED_AUTHORIZATION);
    }

    @Override
    public void intercept(ResponseData data) {
        Assert.notNull(data.getMethod(), "Method is null");
        Assert.notNull(data.getUri(), "Uri is null");
        data.setStatus(CREATED);
        data.setBody(INTERCEPTED_BODY);
    }
}
