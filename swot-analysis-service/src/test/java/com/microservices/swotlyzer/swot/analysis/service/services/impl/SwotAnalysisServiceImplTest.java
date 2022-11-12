package com.microservices.swotlyzer.swot.analysis.service.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.microservices.swotlyzer.common.config.dtos.UserHeaderInfo;
import com.microservices.swotlyzer.common.config.utils.WebClientUtils;
import com.microservices.swotlyzer.swot.analysis.service.dtos.CreateSwotAnalysisDTO;
import com.microservices.swotlyzer.swot.analysis.service.models.SwotAnalysis;
import com.microservices.swotlyzer.swot.analysis.service.models.SwotArea;
import com.microservices.swotlyzer.swot.analysis.service.models.SwotLayoutTypes;
import com.microservices.swotlyzer.swot.analysis.service.repositories.SwotAnalysisRepository;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import web.error.handling.BadRequestException;

@ExtendWith(MockitoExtension.class)
public class SwotAnalysisServiceImplTest {

    private final UserHeaderInfo currentLoggedUser = new UserHeaderInfo(1L, "TESTU_SER");

    @Mock
    private SwotAnalysisRepository swotAnalysisRepository;

    @Mock
    private HttpServletRequest httpServletRequest;

    private AutoCloseable autoCloseable;

    private SwotAnalysisServiceImpl underTest;

    private MockedStatic<WebClientUtils> webClientStaticMocked;

    @BeforeEach
    void setUp() throws IOException {
        webClientStaticMocked = mockStatic(WebClientUtils.class);

        autoCloseable = MockitoAnnotations.openMocks(this);

        underTest = new SwotAnalysisServiceImpl(swotAnalysisRepository, httpServletRequest);
    }

    @AfterEach
    void tearDown() throws Exception {
        webClientStaticMocked.close();
        autoCloseable.close();
    }

    @Test
    @DisplayName("It Should find swot analysis owned by the current logged user.")
    void itShouldFindByCurrentUser() {

    }

    @Test
    // Damn that's a long name
    void itShouldThrowErrorIfPageOrPerPageQuantitiesAreInvalidForFindSwotAnalysisByCurrentUser() {

        when(WebClientUtils.getUserHeadersInfo(any())).thenReturn(currentLoggedUser);
       
        assertThatThrownBy(() -> underTest.findByCurrentUser(-1, -1))
        .isInstanceOf(BadRequestException.class)
        .hasMessageContaining("Invalid quantity for page or per page!");
       
        verify(swotAnalysisRepository, times(0)).findByOwnerId(anyLong(), any());

        
    }

    @Test
    @DisplayName("It Should Create a SwotAnalysis.")
    void itShouldCreateASwotAnalysis() {
        var swotDescription = "Swot Description";
        var layoutType = SwotLayoutTypes.DEFAULT.name();
        var swotTitle = "Swot Title";
        var createSwotAnalysisDTO = CreateSwotAnalysisDTO.builder().swotTemplate(true).description(swotDescription)
                .layoutType(layoutType)
                .title(swotTitle).build();

        var swotStrengthsArea = new SwotArea("Strengths", "green", Collections.emptySet());
        var swotOpportunitiesArea = new SwotArea("Opportunities", "blue", Collections.emptySet());
        var swotWeaknessesArea = new SwotArea("Weaknesses", "red", Collections.emptySet());
        var swotThreatsArea = new SwotArea("Threats", "grey", Collections.emptySet());

        var mockedSwotAnalysis = new SwotAnalysis("1", swotTitle, SwotLayoutTypes.DEFAULT, true,
                swotStrengthsArea, swotWeaknessesArea,
                swotOpportunitiesArea, swotThreatsArea, currentLoggedUser.getUserId());
        when(swotAnalysisRepository.save(any())).thenReturn(mockedSwotAnalysis);

        when(WebClientUtils.getUserHeadersInfo(any()))
                .thenReturn(currentLoggedUser);

        SwotAnalysis newlyCreatedSwotAnalysis = underTest.create(createSwotAnalysisDTO);

        verify(swotAnalysisRepository, times(1)).save(any());
        assertEquals(newlyCreatedSwotAnalysis.getOwnerId(), currentLoggedUser.getUserId());
        assertEquals(newlyCreatedSwotAnalysis, mockedSwotAnalysis);
    }
}
