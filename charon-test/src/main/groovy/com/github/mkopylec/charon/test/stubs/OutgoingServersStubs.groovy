package com.github.mkopylec.charon.test.stubs

import org.springframework.http.HttpStatus

class OutgoingServersStubs {

    private List<OutgoingServer> outgoingServers

    private OutgoingServersStubs(OutgoingServer... outgoingServers) {
        this.outgoingServers = outgoingServers
    }

    static OutgoingServersStubs outgoingServers(OutgoingServer... outgoingServers) {
        return new OutgoingServersStubs(outgoingServers)
    }

    void stubResponse(HttpStatus status) {
        outgoingServers.each { it.stubResponse(status, [:], null, false, 1) }
    }

    void stubResponse(HttpStatus status, int times) {
        outgoingServers.each { it.stubResponse(status, [:], null, false, times) }
    }

    void stubResponse(HttpStatus status, String body, int times) {
        outgoingServers.each { it.stubResponse(status, [:], body, false, times) }
    }

    void stubResponse(HttpStatus status, Map<String, String> headers) {
        outgoingServers.each { it.stubResponse(status, headers, null, false, 1) }
    }

    void resetStubs() {
        outgoingServers.each { it.reset() }
    }
}
