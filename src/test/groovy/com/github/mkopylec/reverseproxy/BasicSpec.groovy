package com.github.mkopylec.reverseproxy

import com.github.mkopylec.recaptcha.security.ResponseData
import com.github.mkopylec.recaptcha.validation.ValidationResult
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.Rule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ContextConfiguration
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import spock.lang.Shared
import spock.lang.Specification

@WebIntegrationTest(randomPort = true)
@ContextConfiguration(loader = SpringApplicationContextLoader, classes = TestApplication)
abstract class BasicSpec extends Specification {

    static final int BACKEND_SERVICE_PORT = 81

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(BACKEND_SERVICE_PORT);

    @Shared
    private RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private EmbeddedWebApplicationContext context

    protected ResponseEntity<String> sendRequest(String path, HttpMethod method, Map<String, String> headers = [:]) {
        def url = "http://localhost:$port/$path"
        def httpHeaders = new HttpHeaders()
        headers.each {name, value -> httpHeaders.add(name, value)}
        def request = new HttpEntity<>(httpHeaders)
        def response = restTemplate.exchange(url, method, request, String)

        return response
    }

    private int getPort() {
        return context.embeddedServletContainer.port
    }
}
