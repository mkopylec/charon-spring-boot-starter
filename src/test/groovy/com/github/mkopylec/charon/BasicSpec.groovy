package com.github.mkopylec.charon

import com.github.mkopylec.charon.application.TestApplication
import com.github.mkopylec.charon.configuration.CharonProperties
import com.github.mkopylec.charon.configuration.CharonProperties.Mapping
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.Rule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate
import spock.lang.Shared
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse
import static com.github.tomakehurst.wiremock.client.WireMock.any
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching
import static org.apache.commons.lang3.StringUtils.EMPTY
import static org.apache.commons.lang3.StringUtils.trimToEmpty
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.ResponseEntity.status

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = TestApplication)
abstract class BasicSpec extends Specification {

    @Rule
    public WireMockRule localhost8080 = new WireMockRule(8080)
    @Rule
    public WireMockRule localhost8081 = new WireMockRule(8081)

    @Shared
    private RestTemplate restTemplate = new RestTemplate()
    @Autowired
    private EmbeddedWebApplicationContext context
    @Autowired
    private CharonProperties charon
    @Autowired
    private ServerProperties server

    protected ResponseEntity<String> sendRequest(HttpMethod method, String uri, Map<String, String> headers = [:], String body = EMPTY) {
        def url = "http://localhost:$context.embeddedServletContainer.port$contextPath$uri"
        def httpHeaders = new HttpHeaders()
        headers.each { name, value -> httpHeaders.put(name, value.split(', ') as List<String>) }
        def request = new HttpEntity<>(body, httpHeaders)
        try {
            return restTemplate.exchange(url, method, request, String)
        } catch (HttpStatusCodeException e) {
            return status(e.getStatusCode())
                    .headers(e.responseHeaders)
                    .body(e.responseBodyAsString);
        }
    }

    protected void addMapping(String name, String path, String... destinations) {
        def mapping = new Mapping(name: name, path: path, destinations: destinations)
        charon.mappings.add(mapping)
    }

    protected String getContextPath() {
        return trimToEmpty(server.contextPath)
    }

    protected String getPort() {
        return context.embeddedServletContainer.port
    }

    protected void stubDestinationResponse(HttpStatus responseStatus) {
        stubResponse(responseStatus)
    }

    protected void stubDestinationResponse(Map<String, String> responseHeaders) {
        stubResponse(OK, responseHeaders)
    }

    protected void stubDestinationResponse(HttpStatus responseStatus, Map<String, String> responseHeaders) {
        stubResponse(responseStatus, responseHeaders)
    }

    protected void stubDestinationResponse(String responseBody) {
        stubResponse(OK, [:], responseBody)
    }

    protected void stubDestinationResponse(HttpStatus responseStatus, String responseBody) {
        stubResponse(responseStatus, [:], responseBody)
    }

    private void stubResponse(HttpStatus responseStatus = OK, Map<String, String> responseHeaders = [:], String responseBody = null) {
        [localhost8080, localhost8081].each {
            def response = aResponse()
            responseHeaders.each { name, value -> response = response.withHeader(name, value) }
            if (responseBody) {
                response = response.withBody(responseBody)
            }
            response = response.withStatus(responseStatus.value())
            it.stubFor(any(urlMatching('.*')).willReturn(response))
        }
    }
}
