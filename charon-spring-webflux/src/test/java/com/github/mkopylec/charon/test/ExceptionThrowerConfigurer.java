package com.github.mkopylec.charon.test;

import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorConfigurer;

class ExceptionThrowerConfigurer extends RequestForwardingInterceptorConfigurer<ExceptionThrower> {

    private ExceptionThrowerConfigurer() {
        super(new ExceptionThrower());
    }

    static ExceptionThrowerConfigurer exceptionThrower() {
        return new ExceptionThrowerConfigurer();
    }
}
