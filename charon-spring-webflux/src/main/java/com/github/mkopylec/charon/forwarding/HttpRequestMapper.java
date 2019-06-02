package com.github.mkopylec.charon.forwarding;

import java.net.URI;

import reactor.core.publisher.Flux;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.BodyInserter;

import static org.springframework.web.reactive.function.BodyInserters.fromDataBuffers;

class HttpRequestMapper {

    URI extractUri(ServerHttpRequest request) {
        return request.getURI();
    }

    HttpMethod extractMethod(ServerHttpRequest request) {
        return request.getMethod();
    }

    HttpHeaders extractHeaders(ServerHttpRequest request) {
        return request.getHeaders();
    }

    BodyInserter<Flux<DataBuffer>, ReactiveHttpOutputMessage> extractBody(ServerHttpRequest request) {
        return fromDataBuffers(request.getBody());
    }
}
