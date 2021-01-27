package com.github.mkopylec.charon.test.stubs

import org.springframework.http.HttpStatus

import static com.github.mkopylec.charon.test.stubs.ResponseDelay.REAL

class OutgoingServersStubs {

    private List<OutgoingServer> outgoingServers

    private OutgoingServersStubs(OutgoingServer... outgoingServers) {
        this.outgoingServers = outgoingServers
    }

    static OutgoingServersStubs outgoingServers(OutgoingServer... outgoingServers) {
        return new OutgoingServersStubs(outgoingServers)
    }

    void stubResponse(HttpStatus status) {
        outgoingServers.each { it.stubResponse(status, [:], null, REAL, 1) }
    }

    void stubResponse(HttpStatus status, int times) {
        outgoingServers.each { it.stubResponse(status, [:], null, REAL, times) }
    }

    void stubResponse(HttpStatus status, String body) {
        outgoingServers.each { it.stubResponse(status, [:], body, REAL, 1) }
    }

    void stubResponse(HttpStatus status, ResponseDelay delay) {
        outgoingServers.each { it.stubResponse(status, [:], null, delay, 1) }
    }

    void stubResponse(HttpStatus status, String body, int times) {
        outgoingServers.each { it.stubResponse(status, [:], body, REAL, times) }
    }

    void stubResponse(HttpStatus status, Map<String, String> headers) {
        outgoingServers.each { it.stubResponse(status, headers, null, REAL, 1) }
    }

    void clearStubs() {
        outgoingServers.each { it.clear() }
    }
}
