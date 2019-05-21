package com.github.mkopylec.charon.test;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
public class ReverseProxyApplication {

    public static void main(String[] args) {
        run(ReverseProxyApplication.class, args);
    }
}
