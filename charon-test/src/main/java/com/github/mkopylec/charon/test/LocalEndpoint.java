package com.github.mkopylec.charon.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class LocalEndpoint {

    @GetMapping("/no/mapping/found")
    String handleNoMappingFound() {
        return "no mapping found response";
    }

    @GetMapping("/default/charon/configuration")
    String handleDefaultCharonConfiguration() {
        return "default charon configuration response";
    }
}
