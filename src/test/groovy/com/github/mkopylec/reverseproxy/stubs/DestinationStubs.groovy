package com.github.mkopylec.reverseproxy.stubs

import com.github.tomakehurst.wiremock.client.RemoteMappingBuilder
import com.github.tomakehurst.wiremock.client.UrlMatchingStrategy
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse
import static com.github.tomakehurst.wiremock.client.WireMock.delete
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo
import static com.github.tomakehurst.wiremock.client.WireMock.get
import static com.github.tomakehurst.wiremock.client.WireMock.head
import static com.github.tomakehurst.wiremock.client.WireMock.options
import static com.github.tomakehurst.wiremock.client.WireMock.post
import static com.github.tomakehurst.wiremock.client.WireMock.put
import static com.github.tomakehurst.wiremock.client.WireMock.trace
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import static org.apache.commons.lang3.StringUtils.EMPTY
import static org.springframework.http.HttpMethod.DELETE
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpMethod.HEAD
import static org.springframework.http.HttpMethod.OPTIONS
import static org.springframework.http.HttpMethod.POST
import static org.springframework.http.HttpMethod.PUT
import static org.springframework.http.HttpMethod.TRACE
import static org.springframework.http.HttpStatus.OK

class DestinationStubs {

    static void stubRequest(HttpMethod method, String uri, WireMockRule... destinations) {
        stubDestinationRequest(method, uri, [:], EMPTY, OK, [:], EMPTY, destinations)
    }

    static void stubRequest(HttpMethod method, String uri, Map<String, String> requestHeaders, WireMockRule... destinations) {
        stubDestinationRequest(method, uri, requestHeaders, EMPTY, OK, [:], EMPTY, destinations)
    }

    static void stubRequest(HttpMethod method, String uri, String requestBody, WireMockRule... destinations) {
        stubDestinationRequest(method, uri, [:], requestBody, OK, [:], EMPTY, destinations)
    }

    private static void stubDestinationRequest(
            HttpMethod httpMethod,
            String uri,
            Map<String, String> requestHeaders,
            String requestBody,
            HttpStatus responseStatus,
            Map<String, String> responseHeaders,
            String responseBody,
            WireMockRule... destinations
    ) {
        destinations.each {
            def stub = method(httpMethod, urlEqualTo(uri))
            requestHeaders.each { k, v -> stub = stub.withHeader(k, equalTo(v)) }
            stub = stub.withRequestBody(equalTo(requestBody))

            def response = aResponse()
            responseHeaders.each { k, v -> response = response.withHeader(k, v) }
            response = response.withStatus(responseStatus.value()).withBody(responseBody)

            it.stubFor(stub.willReturn(response))
        }
    }

    private static RemoteMappingBuilder method(HttpMethod method, UrlMatchingStrategy urlMatchingStrategy) {
        switch (method) {
            case DELETE:
                return delete(urlMatchingStrategy)
            case POST:
                return post(urlMatchingStrategy)
            case GET:
                return get(urlMatchingStrategy)
            case PUT:
                return put(urlMatchingStrategy)
            case OPTIONS:
                return options(urlMatchingStrategy)
            case TRACE:
                return trace(urlMatchingStrategy)
            case HEAD:
                return head(urlMatchingStrategy)
            default:
                throw new RuntimeException("Invalid HTTP method: $method")
        }
    }
}
