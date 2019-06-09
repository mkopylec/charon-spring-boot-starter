package com.github.mkopylec.charon.test.assertions

import com.github.mkopylec.charon.test.stubs.OutgoingServer
import org.springframework.http.ResponseEntity

import static com.github.mkopylec.charon.test.utils.MeterRegistryProvider.meterRegistry

class Assertions {

    static ResponseAssertion assertThat(ResponseEntity response) {
        return new ResponseAssertion(response)
    }

    static OutgoingServerAssertion assertThatServers(OutgoingServer... outgoingServers) {
        return new OutgoingServerAssertion(outgoingServers)
    }

    static MetricsAssertion assertThatMetrics() {
        return new MetricsAssertion(meterRegistry())
    }

    static ExceptionAssertion assertThatException(Throwable throwable) {
        return new ExceptionAssertion(throwable)
    }
}
