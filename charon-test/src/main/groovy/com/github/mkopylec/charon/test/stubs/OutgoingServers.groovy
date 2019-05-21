package com.github.mkopylec.charon.test.stubs

import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

import static org.springframework.http.HttpStatus.OK

class OutgoingServers {

    private List<OutgoingServer> outgoingServers

    OutgoingServers(int ... ports) {
        outgoingServers = ports.collect { new OutgoingServer(it) }
    }

    void stubResponse(boolean responseTimedOut) {
        outgoingServers.each { it.stubResponse(OK, [:], null, responseTimedOut) }
    }

    void stubResponse(HttpStatus status) {
        outgoingServers.each { it.stubResponse(status, [:], null, false) }
    }

    void stubResponse(Map<String, String> headers) {
        outgoingServers.each { it.stubResponse(OK, headers, null, false) }
    }

    void stubResponse(HttpStatus status, Map<String, String> headers) {
        outgoingServers.each { it.stubResponse(status, headers, null, false) }
    }

    void stubResponse(String body) {
        outgoingServers.each { it.stubResponse(OK, [:], body, false) }
    }

    void stubResponse(HttpStatus status, String body) {
        outgoingServers.each { it.stubResponse(status, [:], body, false) }
    }

    void verifyRequest(HttpMethod requestMethod, String requestPath, Map<String, String> requestHeaders) {
        verifyRequest(requestMethod, requestPath, requestHeaders, '')
    }

    void verifyRequest(HttpMethod requestMethod, String requestPath, Map<String, String> requestHeaders, String requestBody) {
        def error = ''
        def matchesCount = outgoingServers.count {
            try {
                it.verifyRequest(requestMethod, requestPath, requestHeaders, requestBody)
                return true
            } catch (AssertionError ex) {
                error += "$ex.message. "
                return false
            }
        }
        if (matchesCount != 1) {
            throw new AssertionError(error.trim())
        }
    }

    void reset() {
        outgoingServers.each { it.reset() }
    }
}
