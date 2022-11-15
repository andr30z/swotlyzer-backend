package com.microservices.swotlyzer.swot.repositories;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.microservices.swotlyzer.swot.Utils.GenerateSwotAnalysis;
import com.microservices.swotlyzer.swot.analysis.service.models.SwotAnalysis;
import com.microservices.swotlyzer.swot.analysis.service.repositories.SwotAnalysisRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;

@DataMongoTest
@ContextConfiguration(classes = { SwotAnalysisRepository.class })
@ComponentScan(basePackages = { "com.microservices.swotlyzer" })
public class SwotAnalysisRepositoryTest {

  @Autowired
  private SwotAnalysisRepository swotAnalysisRepository;

  @Test
  @DisplayName("It should Find swot analyses made by an specific owner")
  void itShouldFindSwotAnalysisMadeByOwnerId() {
    SwotAnalysis firstSwot = GenerateSwotAnalysis.generateSwotAnalysis(
      1L,
      "test1"
    );
    SwotAnalysis secondSwot = GenerateSwotAnalysis.generateSwotAnalysis(
      1L,
      "test2"
    );
    SwotAnalysis thirdSwot = GenerateSwotAnalysis.generateSwotAnalysis(
      2L,
      "test3"
    );
    swotAnalysisRepository.saveAll(List.of(firstSwot, secondSwot, thirdSwot));
    Pageable paging = PageRequest.of(0, 3);
    Page<SwotAnalysis> swotAnalysisByOwner = swotAnalysisRepository.findByOwnerId(
      1L,
      paging
    );
    assertThat(swotAnalysisByOwner.get().collect(Collectors.toList()))
      .hasSize(2)
      .extracting("ownerId")
      .contains(1L)
      .doesNotContain(2L);
  }

  @Test
  @DisplayName("It should find Swot Analysis id and owner id")
  void itShouldFindSwotAnalysisByIdAndOwnerId() {
    SwotAnalysis firstSwot = GenerateSwotAnalysis.generateSwotAnalysis(
      1L,
      "test1"
    );
    SwotAnalysis secondSwot = GenerateSwotAnalysis.generateSwotAnalysis(
      2L,
      "test2"
    );
    swotAnalysisRepository.saveAll(List.of(firstSwot, secondSwot));

    Optional<SwotAnalysis> swotAnalysis = swotAnalysisRepository.findBy_idAndOwnerId(
      "test2",
      2L
    );

    assertTrue(swotAnalysis.isPresent());
    assertEquals(swotAnalysis.get().get_id(), secondSwot.get_id());
    assertEquals(swotAnalysis.get().getOwnerId(), secondSwot.getOwnerId());
  }
}
