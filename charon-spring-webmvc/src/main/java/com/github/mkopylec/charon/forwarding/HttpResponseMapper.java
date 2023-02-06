package com.github.mkopylec.charon.forwarding;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

class HttpResponseMapper {

    void map(ResponseEntity<byte[]> responseEntity, HttpServletResponse response) throws IOException {
        setStatus(responseEntity, response);
        setHeaders(responseEntity, response);
        setBody(responseEntity, response);
    }

    private void setStatus(ResponseEntity<byte[]> responseEntity, HttpServletResponse response) {
        response.setStatus(responseEntity.getStatusCode().value());
    }

    private void setHeaders(ResponseEntity<byte[]> responseEntity, HttpServletResponse response) {
        responseEntity.getHeaders().forEach((name, values) -> values.forEach(value -> response.addHeader(name, value)));
    }

    private void setBody(ResponseEntity<byte[]> responseEntity, HttpServletResponse response) throws IOException {
        if (responseEntity.getBody() != null) {
            response.getOutputStream().write(responseEntity.getBody());
        }
    }
}
