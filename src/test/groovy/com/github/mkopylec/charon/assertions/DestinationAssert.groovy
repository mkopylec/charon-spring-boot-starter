package com.github.mkopylec.charon.assertions

import com.github.tomakehurst.wiremock.junit.WireMockRule

import static com.github.tomakehurst.wiremock.client.RequestPatternBuilder.allRequests

class DestinationAssert {

    private WireMockRule[] actual

    protected DestinationAssert(WireMockRule... actual) {
        this.actual = actual
    }

    ProxiedRequestAssert haveReceivedRequest() {
        return new ProxiedRequestAssert(actual)
    }

    DestinationAssert haveReceivedNoRequest() {
        actual.each {
            it.verify(0, allRequests())
        }
        return this
    }
}
