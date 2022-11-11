package com.microservices.swotlyzer.swot.analysis.service.services.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

@DataMongoTest
@ExtendWith(MockitoExtension.class)
public class SwotAnalysisServiceImplTest {

    @Test
    @DisplayName("It Should Create a SwotAnalysis.")
    public void itShouldCreateASwotAnalysis(){
        
    }
}
