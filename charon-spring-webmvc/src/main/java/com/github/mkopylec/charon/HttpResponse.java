package com.github.mkopylec.charon;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

public class HttpResponse {

    private HttpStatus status;
    private HttpHeaders headers;
    private byte[] body;
}
