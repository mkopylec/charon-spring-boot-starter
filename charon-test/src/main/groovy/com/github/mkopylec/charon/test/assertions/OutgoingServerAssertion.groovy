package com.github.mkopylec.charon.test.assertions

import com.github.mkopylec.charon.test.stubs.OutgoingServer
import org.springframework.http.HttpMethod

class OutgoingServerAssertion {

    private List<OutgoingServer> outgoingServers

    protected OutgoingServerAssertion(OutgoingServer... outgoingServers) {
        this.outgoingServers = outgoingServers
    }

    OutgoingServerAssertion haveNotReceivedRequest() {
        outgoingServers.each { it.verifyNoRequest() }
        return this
    }

    OutgoingServerAssertion haveNotReceivedRequest(Map<String, String> requestHeaders) {
        return haveReceivedRequest(null, null, requestHeaders, '', 0)
    }

    OutgoingServerAssertion haveReceivedRequest(HttpMethod requestMethod, String requestPath) {
        return haveReceivedRequest(requestMethod, requestPath, [:], '', 1)
    }

    OutgoingServerAssertion haveReceivedRequest(HttpMethod requestMethod, String requestPath, int times) {
        return haveReceivedRequest(requestMethod, requestPath, [:], '', times)
    }

    OutgoingServerAssertion haveReceivedRequest(HttpMethod requestMethod, String requestPath, Map<String, String> requestHeaders) {
        return haveReceivedRequest(requestMethod, requestPath, requestHeaders, '', 1)
    }

    OutgoingServerAssertion haveReceivedRequest(HttpMethod requestMethod, String requestPath, Map<String, String> requestHeaders, String requestBody) {
        return haveReceivedRequest(requestMethod, requestPath, requestHeaders, requestBody, 1)
    }

    OutgoingServerAssertion haveReceivedRequest(HttpMethod requestMethod, String requestPath, Map<String, String> requestHeaders, String requestBody, int times) {
        def error = '\r\n'
        def matchesCount = outgoingServers.count {
            try {
                it.verifyRequest(requestMethod, requestPath, requestHeaders, requestBody, times)
                return true
            } catch (AssertionError ex) {
                error += "\r\n[$it]\r\n$ex.message\r\n"
                return false
            }
        }
        if (times == 0) {
            if (matchesCount < outgoingServers.size()) {
                throw new AssertionError(error)
            } else {
                return this
            }
        }
        if (matchesCount != 1) {
            throw new AssertionError(error)
        }
        return this
    }
}
