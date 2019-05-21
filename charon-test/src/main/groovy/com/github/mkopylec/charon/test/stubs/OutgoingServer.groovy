package com.github.mkopylec.charon.test.stubs

import org.mockserver.integration.ClientAndServer
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

import static org.mockserver.integration.ClientAndServer.startClientAndServer
import static org.mockserver.model.Delay.seconds
import static org.mockserver.model.Header.header
import static org.mockserver.model.HttpRequest.request
import static org.mockserver.model.HttpResponse.response

class OutgoingServer {

    private ClientAndServer server

    protected OutgoingServer(int port) {
        server = startClientAndServer(port)
    }

    protected void stubResponse(HttpStatus status, Map<String, String> headers, String body, boolean timedOut) {
        def responseHeaders = headers.collect { header(it.key, it.value) }
        server.when(request('.*'))
                .respond(response(body)
                .withStatusCode(status.value())
                .withHeaders(responseHeaders)
                .withDelay(seconds(timedOut ? 1 : 0)))
    }

    protected void verifyRequest(HttpMethod method, String path, Map<String, String> headers, String body) {
        def requestHeaders = headers.collect { header(it.key, it.value) }
        server.verify(request(path)
                .withMethod(method.name())
                .withHeaders(requestHeaders)
                .withBody(body))
    }

    protected void reset() {
        server.reset()
    }
}
