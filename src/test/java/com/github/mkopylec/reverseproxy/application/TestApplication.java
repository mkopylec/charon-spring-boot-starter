package com.github.mkopylec.reverseproxy.application;

import com.github.mkopylec.reverseproxy.configuration.EnableReverseProxy;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.springframework.boot.SpringApplication.run;

@EnableReverseProxy
@SpringBootApplication
public class TestApplication {

    public static void main(String[] args) {
        run(TestApplication.class, args);
    }
}
