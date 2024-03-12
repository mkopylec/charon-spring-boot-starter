package com.github.mkopylec.charon.test.specification

import com.github.mkopylec.charon.test.stubs.OutgoingServer
import com.github.mkopylec.charon.test.utils.HttpClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

import static com.github.mkopylec.charon.test.stubs.OutgoingServersStubs.outgoingServers
import static com.github.mkopylec.charon.test.utils.MeterRegistryProvider.clearMetrics
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestPropertySource(properties = ['default-charon-configuration = false'])
abstract class BasicSpec extends Specification {

    protected static OutgoingServer localhost8080 = new OutgoingServer(8080)
    protected static OutgoingServer localhost8081 = new OutgoingServer(8081)

    @Autowired
    protected HttpClient http

    void cleanup() {
//        outgoingServers(localhost8080, localhost8081).clearStubs()
        clearMetrics()
    }
}
