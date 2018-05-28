package com.github.mkopylec.charon.assertions

import com.github.mkopylec.charon.application.GraphiteServerMock
import com.github.mkopylec.charon.application.TestForwardedRequestInterceptor
import com.github.mkopylec.charon.application.TestTraceInterceptor
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.springframework.http.ResponseEntity

class Assertions {

    static DestinationAssert assertThat(WireMockRule... destinations) {
        return new DestinationAssert(destinations)
    }

    static ResponseAssert assertThat(ResponseEntity response) {
        return new ResponseAssert(response)
    }

    static MetricsAssert assertThat(GraphiteServerMock graphiteServer) {
        return new MetricsAssert(graphiteServer)
    }

    static TraceAssert assertThat(TestTraceInterceptor traceInterceptor) {
        return new TraceAssert(traceInterceptor)
    }

    static InterceptorAssert assertThat(TestForwardedRequestInterceptor interceptor) {
        return new InterceptorAssert(interceptor);
    }
}
