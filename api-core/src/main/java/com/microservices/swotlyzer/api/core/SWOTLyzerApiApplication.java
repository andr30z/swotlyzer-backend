package com.microservices.swotlyzer.api.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableMongoAuditing
@EnableWebMvc
@SpringBootApplication
@ComponentScan(basePackages = "com.microservices.swotlyzer")
public class SWOTLyzerApiApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(SWOTLyzerApiApplication.class, args);
    }

}
