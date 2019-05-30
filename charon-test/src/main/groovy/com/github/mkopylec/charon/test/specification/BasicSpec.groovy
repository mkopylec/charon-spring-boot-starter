package com.github.mkopylec.charon.test.specification

import com.github.mkopylec.charon.test.stubs.OutgoingServer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import spock.lang.Specification

import static com.github.mkopylec.charon.test.stubs.MeterRegistryProvider.resetMetrics
import static com.github.mkopylec.charon.test.stubs.OutgoingServersStubs.outgoingServers
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

// TODO Tests for exceptions
@SpringBootTest(webEnvironment = RANDOM_PORT)
abstract class BasicSpec extends Specification {

    protected static OutgoingServer localhost8080 = new OutgoingServer(8080)
    protected static OutgoingServer localhost8081 = new OutgoingServer(8081)

    @Autowired
    private TestRestTemplate restTemplate // TODO RequestSender.groovy

    protected ResponseEntity<String> sendRequest(HttpMethod method, String path) {
        return sendRequest(method, path, [:], '')
    }

    protected ResponseEntity<String> sendRequest(HttpMethod method, String path, Map<String, String> headers) {
        return sendRequest(method, path, headers, '')
    }

    protected ResponseEntity<String> sendRequest(HttpMethod method, String path, Map<String, String> headers, String body) {
        def httpHeaders = new HttpHeaders()
        headers.each { name, value -> httpHeaders.put(name, value.split(', ') as List<String>) }
        def request = new HttpEntity<>(body, httpHeaders)
        return restTemplate.exchange(path, method, request, String)
    }

    void cleanup() {
        outgoingServers(localhost8080, localhost8081)
                .resetStubs()
        resetMetrics()
    }
}
