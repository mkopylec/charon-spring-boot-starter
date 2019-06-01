package com.github.mkopylec.charon.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class LocalEndpoint {

    @GetMapping("/no/mapping/found")
    String getLocalResponse() {
        return "local endpoint response";
    }
}
