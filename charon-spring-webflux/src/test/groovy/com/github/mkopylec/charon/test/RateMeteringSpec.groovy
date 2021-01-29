package com.github.mkopylec.charon.test

import com.github.mkopylec.charon.test.specification.RateMeteringBasicSpec

class RateMeteringSpec extends RateMeteringBasicSpec {

    @Override
    protected String meteredExceptionName() {
        return 'searchdomainunknownhostexception'
    }
}
