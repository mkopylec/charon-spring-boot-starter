package com.github.mkopylec.charon.specification

import com.github.mkopylec.charon.BasicSpec
import com.github.mkopylec.charon.core.http.RequestData
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod

class RequestDataSpec extends BasicSpec {

    def "Should have a functional constructor"() {
        given:
        def method = HttpMethod.GET
        def uri = ""
        def headers = new HttpHeaders()
        def body = "".bytes

        when:
        def requestData = new RequestData(method, uri, headers, body)

        then:
        requestData.getMethod() == method
        requestData.getUri() == uri
        requestData.getHeaders() == headers
        requestData.getBody() == body
    }

    def "Should have functional setters"() {
        given:
        def method = HttpMethod.GET
        def uri = ""
        def headers = new HttpHeaders()
        def body = "".bytes
        def requestData = new RequestData(null, null, null, null)

        when:
        requestData.setMethod(method)
        requestData.setUri(uri)
        requestData.setHeaders(headers)
        requestData.setBody(body)

        then:
        requestData.getMethod() == method
        requestData.getUri() == uri
        requestData.getHeaders() == headers
        requestData.getBody() == body
    }
}
