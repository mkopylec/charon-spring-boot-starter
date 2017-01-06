package com.github.mkopylec.charon.application;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
public class TestController {

    public static final String SAMPLE_HEADER = "header";

    @PostMapping("/not/mapped/uri")
    public ResponseEntity<String> setMessage(@RequestHeader(SAMPLE_HEADER) String header, @RequestBody String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(SAMPLE_HEADER, header);
        return new ResponseEntity<>(message, headers, OK);
    }
}
