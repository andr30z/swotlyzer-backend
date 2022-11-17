package com.microservices.swotlyzer.swot.analysis.service.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.microservices.swotlyzer.swot.analysis.service.dtos.PaginationResponse;
import com.microservices.swotlyzer.swot.analysis.service.models.SwotAnalysis;
import com.microservices.swotlyzer.swot.analysis.service.repositories.SwotAnalysisRepository;
import com.microservices.swotlyzer.swot.testutils.GenerateSwotAnalysis;

@DataMongoTest
public class PaginationUtilTest {

    @Autowired
    private SwotAnalysisRepository swotAnalysisRepository;

    private static PaginationUtil<SwotAnalysis, SwotAnalysisRepository> underTest = new PaginationUtil<SwotAnalysis, SwotAnalysisRepository>();

    @Test
    void itShouldBuildAPaginationResponse() {

        SwotAnalysis firstSwot = GenerateSwotAnalysis.generateSwotAnalysis(1L, "1");
        SwotAnalysis secondSwot = GenerateSwotAnalysis.generateSwotAnalysis(1L, "2");
        SwotAnalysis thirdtSwot = GenerateSwotAnalysis.generateSwotAnalysis(1L, "3");
        var totalPages = 3;
        var currentPage = 0;
        var pageSize = 3;
        PaginationResponse<SwotAnalysis> paginationResponse = underTest.buildResponse(new PageImpl<SwotAnalysis>(
                List.of(firstSwot, secondSwot, thirdtSwot),
                PageRequest.of(currentPage, pageSize),
                totalPages));

        // build response method adds 1 to the current page for better readability
        assertEquals(paginationResponse.getCurrentPage(), currentPage + 1);
        assertEquals(paginationResponse.getPerPage(), pageSize);
        assertEquals(paginationResponse.getTotalItems(), 3);
        assertThat(paginationResponse.getData())
                .hasSize(3)
                .extracting("_id")
                .contains("1", "2", "3");

    }

    // nome do servidor, cargo,

    @Test
    void itShouldPaginateAll() {
        SwotAnalysis firstSwot = GenerateSwotAnalysis.generateSwotAnalysis(1L, "1");
        SwotAnalysis secondSwot = GenerateSwotAnalysis.generateSwotAnalysis(1L, "2");
        SwotAnalysis thirdtSwot = GenerateSwotAnalysis.generateSwotAnalysis(1L, "3");
        SwotAnalysis fourthSwot = GenerateSwotAnalysis.generateSwotAnalysis(1L, "4");
        SwotAnalysis fifthSwot = GenerateSwotAnalysis.generateSwotAnalysis(1L, "5");
        SwotAnalysis sixthSwot = GenerateSwotAnalysis.generateSwotAnalysis(1L, "6");
        SwotAnalysis seventhtSwot = GenerateSwotAnalysis.generateSwotAnalysis(1L, "7");
        swotAnalysisRepository.saveAll(List.of(firstSwot,
                secondSwot,
                thirdtSwot,
                fourthSwot,
                fifthSwot,
                sixthSwot,
                seventhtSwot));
        // 0==1
        PaginationResponse<SwotAnalysis> paginationResponse = underTest.paginateAll(0, 5, swotAnalysisRepository);
        assertThat(paginationResponse.getData())
                .hasSize(5)
                .extracting("_id")
                .contains("1", "2", "3", "4", "5")
                .doesNotContain("6", "7");

    }
}
