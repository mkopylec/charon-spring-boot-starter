package com.github.mkopylec.charon.forwarding;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

class HttpResponseMapper {

    Mono<Void> map(ClientResponse clientResponse, ServerHttpResponse response) {
        setStatus(clientResponse, response);
        setHeaders(clientResponse, response);
        return setBody(clientResponse, response);
    }

    private void setStatus(ClientResponse clientResponse, ServerHttpResponse response) {
        response.setStatusCode(clientResponse.statusCode());
    }

    private void setHeaders(ClientResponse clientResponse, ServerHttpResponse response) {
        clientResponse.headers().asHttpHeaders().forEach((name, values) -> values.forEach(value -> response.getHeaders().put(name, values)));
    }

    private Mono<Void> setBody(ClientResponse clientResponse, ServerHttpResponse response) {
        Mono<DataBuffer> body = clientResponse.bodyToMono(byte[].class)
                .map(bytes -> response.bufferFactory().wrap(bytes));
        return response.writeWith(body);
    }
}
