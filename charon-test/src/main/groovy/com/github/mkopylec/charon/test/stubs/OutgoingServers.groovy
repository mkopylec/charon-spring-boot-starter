package com.github.mkopylec.charon.test.stubs

import org.springframework.http.HttpStatus

class OutgoingServers {

    private List<OutgoingServer> outgoingServers

    OutgoingServers(int ... ports) {
        outgoingServers = ports.collect { new OutgoingServer(it) }
    }

    void stubResponse(boolean timedOut) {
        outgoingServers.each { it.stubResponse(timedOut) }
    }

    void stubResponse(HttpStatus responseStatus) {
        outgoingServers.each { it.stubResponse(responseStatus) }
    }

    void stubResponse(Map<String, String> responseHeaders) {
        outgoingServers.each { it.stubResponse(responseHeaders) }
    }

    void stubResponse(HttpStatus responseStatus, Map<String, String> responseHeaders) {
        outgoingServers.each { it.stubResponse(responseStatus, responseHeaders) }
    }

    void stubResponse(String responseBody) {
        outgoingServers.each { it.stubResponse(responseBody) }
    }

    void stubResponse(HttpStatus responseStatus, String responseBody) {
        outgoingServers.each { it.stubResponse(responseStatus, responseBody) }
    }

    void reset() {
        outgoingServers.each { it.reset() }
    }
}
