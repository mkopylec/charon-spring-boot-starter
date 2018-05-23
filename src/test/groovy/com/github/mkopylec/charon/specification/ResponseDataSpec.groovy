package com.github.mkopylec.charon.specification

import com.github.mkopylec.charon.BasicSpec
import com.github.mkopylec.charon.core.http.RequestData
import com.github.mkopylec.charon.core.http.ResponseData
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus

class ResponseDataSpec extends BasicSpec {

    def "Should have a functional constructor"() {
        given:
        def status = HttpStatus.OK
        def headers = new HttpHeaders()
        def body = "".bytes
        def requestData = new RequestData(null, null, null, null)

        when:
        def responseData = new ResponseData(status, headers, body, requestData)

        then:
        responseData.getStatus() == status
        responseData.getHeaders() == headers
        responseData.getBody() == body
        responseData.getRequestData() == requestData
    }

    def "Should have functional setters"() {
        given:
        def status = HttpStatus.OK
        def headers = new HttpHeaders()
        def body = "".bytes
        def requestData = new RequestData(null, null, null, null)
        def responseData = new ResponseData(null, new HttpHeaders(), null, null)

        when:
        responseData.setStatus(status)
        responseData.setHeaders(headers)
        responseData.setBody(body)
        responseData.setRequestData(requestData)

        then:
        responseData.getStatus() == status
        responseData.getHeaders() == headers
        responseData.getBody() == body
        responseData.getRequestData() == requestData
    }
}
