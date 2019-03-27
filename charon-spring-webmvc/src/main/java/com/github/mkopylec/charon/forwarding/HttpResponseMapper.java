package com.github.mkopylec.charon.forwarding;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

class HttpResponseMapper {

    void map(HttpResponse httpResponse, HttpServletResponse response) throws IOException {
        response.setStatus(httpResponse.getStatus().value());
        httpResponse.getHeaders().forEach((name, values) -> values.forEach(value -> response.addHeader(name, value)));
        if (httpResponse.getBody() != null) {
            response.getOutputStream().write(httpResponse.getBody());
        }
    }

    HttpResponse map(ResponseEntity<byte[]> response) {
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(response.getHeaders());
        return new HttpResponse(response.getStatusCode(), headers, response.getBody());
    }
}
