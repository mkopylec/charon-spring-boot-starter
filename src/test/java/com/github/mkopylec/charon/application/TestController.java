package com.github.mkopylec.charon.application;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    public static final String SAMPLE_MESSAGE = "Sample message";

    @RequestMapping("/not/mapped/uri")
    public String getMessage() {
        return SAMPLE_MESSAGE;
    }
}
