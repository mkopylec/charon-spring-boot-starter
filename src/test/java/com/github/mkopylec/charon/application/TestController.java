package com.github.mkopylec.charon.application;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class TestController {

    public static final String SAMPLE_HEADER = "header";

    @RequestMapping(value = "/not/mapped/uri", method = POST)
    public ResponseEntity<String> setMessage(@RequestHeader(SAMPLE_HEADER) String header, @RequestBody String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(SAMPLE_HEADER, header);
        return new ResponseEntity<>(message, headers, OK);
    }
}
