package com.github.mkopylec.charon.forwarding.interceptors;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ClientHttpResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyExtractor;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static com.github.mkopylec.charon.forwarding.RequestForwardingException.requestForwardingError;
import static com.github.mkopylec.charon.forwarding.Utils.copyHeaders;
import static org.springframework.web.reactive.function.client.ClientResponse.create;
import static reactor.core.publisher.Mono.empty;
import static reactor.core.publisher.Mono.just;

public class HttpResponse implements ClientResponse {

    private Mono<byte[]> body;
    private ClientResponse delegate;
    private HttpRequest request;

    public HttpResponse(HttpStatusCode status) {
        this(status, null);
    }

    public HttpResponse(HttpStatusCode status, HttpRequest request) {
        body = empty();
        delegate = create(status).build();
        this.request = request;
    }

    HttpResponse(ClientResponse response, HttpRequest request) {
        body = response.bodyToMono(byte[].class); // Releases connection
        delegate = response;
        this.request = request;
    }

    @Override
    public HttpStatusCode statusCode() {
        return delegate.statusCode();
    }

    public void setStatusCode(HttpStatusCode status) {
        delegate = delegate.mutate()
                .statusCode(status)
                .build();
    }

    @Override
    public Headers headers() {
        return delegate.headers();
    }

    public void setHeaders(HttpHeaders headers) {
        delegate = delegate.mutate()
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

    @Override
    public org.springframework.http.HttpRequest request() {
        return request != null ? new org.springframework.http.HttpRequest() {

            @Override
            public HttpMethod getMethod() {
                return request.method();
            }

            @Override
            public URI getURI() {
                return request.url();
            }

            @Override
            public HttpHeaders getHeaders() {
                return request.headers();
            }

            @Override
            public Map<String, Object> getAttributes() {
                return request.attributes();
            }
        } : null;
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
    public Mono<Void> releaseBody() {
        return empty(); // Already released in constructor
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

    @Override
    public Mono<ResponseEntity<Void>> toBodilessEntity() {
        return delegate.toBodilessEntity();
    }

    @Override
    public Mono<WebClientResponseException> createException() {
        return delegate.createException();
    }

    @Override
    public <T> Mono<T> createError() {
        return delegate.createError();
    }

    @Override
    public String logPrefix() {
        return delegate.logPrefix();
    }
}
