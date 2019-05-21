package com.github.mkopylec.charon.test.stubs

import org.mockserver.integration.ClientAndServer
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

import static org.mockserver.integration.ClientAndServer.startClientAndServer
import static org.mockserver.model.Delay.seconds
import static org.mockserver.model.Header.header
import static org.mockserver.model.HttpRequest.request
import static org.mockserver.model.HttpResponse.response
import static org.springframework.http.HttpStatus.OK

class OutgoingServer {

    private ClientAndServer server

    protected OutgoingServer(int port) {
        server = startClientAndServer(port)
    }

    protected void stubResponse(boolean responseTimedOut) {
        stub(OK, [:], null, responseTimedOut)
    }

    protected void stubResponse(HttpStatus responseStatus) {
        stub(responseStatus, [:], null, false)
    }

    protected void stubResponse(Map<String, String> responseHeaders) {
        stub(OK, responseHeaders, null, false)
    }

    protected void stubResponse(HttpStatus responseStatus, Map<String, String> responseHeaders) {
        stub(responseStatus, responseHeaders, null, false)
    }

    protected void stubResponse(String responseBody) {
        stub(OK, [:], responseBody, false)
    }

    protected void stubResponse(HttpStatus responseStatus, String responseBody) {
        stub(responseStatus, [:], responseBody, false)
    }

    protected void verifyRequest(HttpMethod requestMethod, String requestPath, Map<String, String> requestHeaders, String requestBody) {
        def headers = requestHeaders.collect { header(it.key, it.value) }
        server.verify(request(requestPath)
                .withMethod(requestMethod.name())
                .withHeaders(headers)
                .withBody(requestBody))
    }

    protected void reset() {
        server.reset()
    }

    private void stub(HttpStatus responseStatus, Map<String, String> responseHeaders, String responseBody, boolean responseTimedOut) {
        def headers = responseHeaders.collect { header(it.key, it.value) }
        server.when(request('.*'))
                .respond(response(responseBody)
                .withStatusCode(responseStatus.value())
                .withHeaders(headers)
                .withDelay(seconds(responseTimedOut ? 1 : 0)))
    }
}
