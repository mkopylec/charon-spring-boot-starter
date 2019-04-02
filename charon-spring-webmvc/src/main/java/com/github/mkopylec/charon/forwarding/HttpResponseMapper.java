package com.github.mkopylec.charon.forwarding;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;

class HttpResponseMapper {

    void map(ResponseEntity<byte[]> responseEntity, HttpServletResponse response) throws IOException {
        response.setStatus(responseEntity.getStatusCodeValue());
        responseEntity.getHeaders().forEach((name, values) -> values.forEach(value -> response.addHeader(name, value)));
        if (responseEntity.getBody() != null) {
            response.getOutputStream().write(responseEntity.getBody());
        }
    }
}
