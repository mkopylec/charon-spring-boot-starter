package com.github.mkopylec.reverseproxy.assertions

import com.github.tomakehurst.wiremock.junit.WireMockRule

class Assertions {

    static RequestDestinationAssert assertThatRequestWasProxiedTo(WireMockRule... destinations) {
        return new RequestDestinationAssert(destinations)
    }
}
