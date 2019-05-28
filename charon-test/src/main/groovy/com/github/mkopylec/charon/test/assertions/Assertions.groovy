package com.github.mkopylec.charon.test.assertions

import com.github.mkopylec.charon.test.stubs.OutgoingServer
import org.springframework.http.ResponseEntity

class Assertions {

    static ResponseAssertion assertThat(ResponseEntity response) {
        return new ResponseAssertion(response)
    }

    static OutgoingServerAssertion assertThatOneOf(OutgoingServer... outgoingServers) {
        return new OutgoingServerAssertion(outgoingServers)
    }
}
