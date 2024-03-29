package com.microservices.swotlyzer.swot.analysis.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoAuditing
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableMongoRepositories
@ComponentScan(basePackages = "com.microservices.swotlyzer")
public class SWOTAnalysisServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SWOTAnalysisServiceApplication.class, args);
    }

}
