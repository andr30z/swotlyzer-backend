package com.microservices.swotlyzer.api.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableMongoAuditing
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableEurekaClient
@EnableMongoRepositories(basePackages = "com.microservices.swotlyzer.api.core.repositories")
@ComponentScan(basePackages = "com.microservices.swotlyzer")
public class SWOTLyzerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SWOTLyzerApiApplication.class, args);
    }

}
