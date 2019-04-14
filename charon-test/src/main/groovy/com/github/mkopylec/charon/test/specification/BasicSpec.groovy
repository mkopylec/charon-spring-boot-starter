package com.github.mkopylec.charon.test.specification

import com.github.mkopylec.charon.test.stubs.OutgoingServers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(webEnvironment = RANDOM_PORT)
abstract class BasicSpec extends Specification {

    protected static OutgoingServers outgoingServers = new OutgoingServers(8080, 8081)
    @Autowired
    private TestRestTemplate restTemplate

    protected ResponseEntity<String> sendRequest(HttpMethod method, String path, Map<String, String> headers = [:], String body = '') {
        def httpHeaders = new HttpHeaders()
        headers.each { name, value -> httpHeaders.put(name, value.split(', ') as List<String>) }
        def request = new HttpEntity<>(body, httpHeaders)
        return restTemplate.exchange(path, method, request, String)
    }

    void cleanup() {
        outgoingServers.reset()
    }
}
