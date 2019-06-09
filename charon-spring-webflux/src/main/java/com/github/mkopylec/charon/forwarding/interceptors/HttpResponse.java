package com.github.mkopylec.charon.forwarding.interceptors;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ClientHttpResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyExtractor;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.github.mkopylec.charon.forwarding.RequestForwardingException.requestForwardingError;
import static com.github.mkopylec.charon.forwarding.Utils.copyHeaders;
import static org.springframework.web.reactive.function.client.ClientResponse.create;
import static org.springframework.web.reactive.function.client.ClientResponse.from;
import static reactor.core.publisher.Mono.empty;
import static reactor.core.publisher.Mono.just;

public class HttpResponse implements ClientResponse {

    private Mono<byte[]> body;
    private ClientResponse delegate;

    public HttpResponse(HttpStatus status) {
        body = empty();
        delegate = create(status).build();
    }

    HttpResponse(ClientResponse response) {
        body = response.bodyToMono(byte[].class);
        delegate = response;
    }

    @Override
    public HttpStatus statusCode() {
        return delegate.statusCode();
    }

    @Override
    public int rawStatusCode() {
        return delegate.rawStatusCode();
    }

    public void setStatusCode(HttpStatus status) {
        delegate = from(delegate)
                .statusCode(status)
                .build();
    }

    @Override
    public Headers headers() {
        return delegate.headers();
    }

    public void setHeaders(HttpHeaders headers) {
        delegate = from(delegate)
                .headers(httpHeaders -> {
                    httpHeaders.clear();
                    httpHeaders.putAll(headers);
                })
                .build();
    }

    @Override
    public MultiValueMap<String, ResponseCookie> cookies() {
        return delegate.cookies();
    }

    @Override
    public ExchangeStrategies strategies() {
        return delegate.strategies();
    }

    public Mono<byte[]> getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = just(body);
        HttpHeaders rewrittenHeaders = copyHeaders(delegate.headers().asHttpHeaders());
        rewrittenHeaders.setContentLength(body.length);
        setHeaders(rewrittenHeaders);
    }

    @Override
    public <T> T body(BodyExtractor<T, ? super ClientHttpResponse> extractor) {
        throw requestForwardingError("Method not implemented");
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Mono<T> bodyToMono(Class<? extends T> elementClass) {
        if (byte[].class.isAssignableFrom(elementClass)) {
            return (Mono<T>) getBody();
        }
        throw requestForwardingError("Method not implemented");
    }

    @Override
    public <T> Mono<T> bodyToMono(ParameterizedTypeReference<T> typeReference) {
        throw requestForwardingError("Method not implemented");
    }

    @Override
    public <T> Flux<T> bodyToFlux(Class<? extends T> elementClass) {
        throw requestForwardingError("Method not implemented");
    }

    @Override
    public <T> Flux<T> bodyToFlux(ParameterizedTypeReference<T> typeReference) {
        throw requestForwardingError("Method not implemented");
    }

    @Override
    public <T> Mono<ResponseEntity<T>> toEntity(Class<T> bodyType) {
        throw requestForwardingError("Method not implemented");
    }

    @Override
    public <T> Mono<ResponseEntity<T>> toEntity(ParameterizedTypeReference<T> typeReference) {
        throw requestForwardingError("Method not implemented");
    }

    @Override
    public <T> Mono<ResponseEntity<List<T>>> toEntityList(Class<T> elementType) {
        throw requestForwardingError("Method not implemented");
    }

    @Override
    public <T> Mono<ResponseEntity<List<T>>> toEntityList(ParameterizedTypeReference<T> typeReference) {
        throw requestForwardingError("Method not implemented");
    }
}
