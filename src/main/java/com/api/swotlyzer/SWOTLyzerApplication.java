package com.api.swotlyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableMongoAuditing
@SpringBootApplication
public class SWOTLyzerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SWOTLyzerApplication.class, args);
    }
}
