package com.api.swotlyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@EnableMongoAuditing
@EnableWebMvc
@SpringBootApplication
public class SWOTLyzerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SWOTLyzerApplication.class, args);
    }

}
