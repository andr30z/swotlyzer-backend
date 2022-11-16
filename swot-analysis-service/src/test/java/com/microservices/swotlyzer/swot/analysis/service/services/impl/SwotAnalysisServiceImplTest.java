package com.microservices.swotlyzer.swot.analysis.service.services.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.microservices.swotlyzer.common.config.dtos.UserHeaderInfo;
import com.microservices.swotlyzer.common.config.utils.WebClientUtils;
import com.microservices.swotlyzer.swot.analysis.service.dtos.CreateSwotAnalysisDTO;
import com.microservices.swotlyzer.swot.analysis.service.dtos.UpdateSwotAnalysisDTO;
import com.microservices.swotlyzer.swot.analysis.service.models.SwotAnalysis;
import com.microservices.swotlyzer.swot.analysis.service.models.SwotLayoutTypes;
import com.microservices.swotlyzer.swot.analysis.service.repositories.SwotAnalysisRepository;
import com.microservices.swotlyzer.swot.testutils.GenerateSwotAnalysis;

import web.error.handling.BadRequestException;
import web.error.handling.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
public class SwotAnalysisServiceImplTest {

  private final UserHeaderInfo currentLoggedUser = new UserHeaderInfo(
      1L,
      "TEST_USER");

  @Mock
  private SwotAnalysisRepository swotAnalysisRepository;

  @Captor
  private ArgumentCaptor<Pageable> pageableArgumentCaptor;

  @Captor
  private ArgumentCaptor<String> stringArgumentCaptor;

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
  @DisplayName("It should find a swot analysis of the current user")
  public void itShouldFindASwotAnalysisByTheCurrentUser() {
    SwotAnalysis swotAnalysisCurrentUser = GenerateSwotAnalysis.generateSwotAnalysis(
        currentLoggedUser.getUserId(),
        "1000");
    when(WebClientUtils.getUserHeadersInfo(any()))
        .thenReturn(currentLoggedUser);

    when(swotAnalysisRepository.findBy_idAndOwnerId(anyString(), anyLong()))
        .thenReturn(Optional.of(swotAnalysisCurrentUser));

    SwotAnalysis swotAnalysis = underTest.getSwotAnalysisByCurrentUser(
        swotAnalysisCurrentUser.get_id());
    verify(swotAnalysisRepository, times(1))
        .findBy_idAndOwnerId(stringArgumentCaptor.capture(), any());
    assertEquals(
        stringArgumentCaptor.getValue(),
        swotAnalysisCurrentUser.get_id());
    assertEquals(swotAnalysis, swotAnalysisCurrentUser);
  }

  @Test
  @DisplayName(
    "It should throw an error when a swot analysis is not owned by the current user"
  )
  public void itShouldThrowErrorWhenSwotAnalysisByIsNotOwnedByTheCurrentUser() {
    when(WebClientUtils.getUserHeadersInfo(any()))
      .thenReturn(currentLoggedUser);

    when(swotAnalysisRepository.findBy_idAndOwnerId(anyString(), anyLong()))
      .thenReturn(Optional.empty());

    assertThatThrownBy(() -> underTest.getSwotAnalysisByCurrentUser("9999"))
      .isInstanceOf(ResourceNotFoundException.class)
      .hasMessageContaining(
        "SWOT Analysis doesn't exist or doesn't" +
        "belongs to user with id: " +
        currentLoggedUser.getUserId()
      );
  }

  @Test
  @DisplayName("It should delete the current user swot analysis")
  public void itShouldDeleteASwotAnalysis() {
    SwotAnalysis swotAnalysisCurrentUser = GenerateSwotAnalysis.generateSwotAnalysis(
        currentLoggedUser.getUserId(),
        "1000");
    when(WebClientUtils.getUserHeadersInfo(any()))
        .thenReturn(currentLoggedUser);

    when(swotAnalysisRepository.findBy_idAndOwnerId(anyString(), anyLong()))
        .thenReturn(Optional.of(swotAnalysisCurrentUser));

    doNothing().when(swotAnalysisRepository).delete(swotAnalysisCurrentUser);

    underTest.deleteSwotAnalysisByCurrentUser(swotAnalysisCurrentUser.get_id());
    verify(swotAnalysisRepository, times(1))
        .findBy_idAndOwnerId(stringArgumentCaptor.capture(), any());
    verify(swotAnalysisRepository, times(1)).delete(swotAnalysisCurrentUser);
    assertEquals(
        stringArgumentCaptor.getValue(),
        swotAnalysisCurrentUser.get_id());
  }

  @Test
  @DisplayName("It Should find swot analyses owned by the logged user.")
  void itShouldFindByCurrentUser() {
    // saving in real repository
    SwotAnalysis swotAnalysisCurrentUser = GenerateSwotAnalysis.generateSwotAnalysis(
        currentLoggedUser.getUserId(),
        "1000");

    when(WebClientUtils.getUserHeadersInfo(any()))
        .thenReturn(currentLoggedUser);

    when(swotAnalysisRepository.findByOwnerId(anyLong(), any()))
        .thenReturn(
            new PageImpl<SwotAnalysis>(
                List.of(swotAnalysisCurrentUser),
                PageRequest.of(0, 1),
                1));

    underTest.findByCurrentUser(1, 5);

    verify(swotAnalysisRepository, times(1))
        .findByOwnerId(anyLong(), pageableArgumentCaptor.capture());

    Pageable pageable = pageableArgumentCaptor.getValue();

    // testing for 0 because the index for database pageable queries starts at 0 and
    // I'm passing 1 as page (1-1 = first page)
    assertEquals(pageable.getPageNumber(), 0);
    assertEquals(pageable.getPageSize(), 5);
  }

  @Test
  // Damn that's a long name
  void itShouldThrowErrorIfPageOrPerPageQuantitiesAreInvalidForFindSwotAnalysisByCurrentUser() {
    when(WebClientUtils.getUserHeadersInfo(any()))
      .thenReturn(currentLoggedUser);

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
    var createSwotAnalysisDTO = CreateSwotAnalysisDTO
        .builder()
        .swotTemplate(true)
        .description(swotDescription)
        .layoutType(layoutType)
        .title(swotTitle)
        .build();

    SwotAnalysis mockedSwotAnalysis = GenerateSwotAnalysis.generateSwotAnalysis(
        currentLoggedUser.getUserId(),
        "1000");
    when(swotAnalysisRepository.save(any())).thenReturn(mockedSwotAnalysis);

    when(WebClientUtils.getUserHeadersInfo(any()))
        .thenReturn(currentLoggedUser);

    SwotAnalysis newlyCreatedSwotAnalysis = underTest.create(
        createSwotAnalysisDTO);

    verify(swotAnalysisRepository, times(1)).save(any());

    assertEquals(
        newlyCreatedSwotAnalysis.getOwnerId(),
        currentLoggedUser.getUserId());
    assertEquals(newlyCreatedSwotAnalysis, mockedSwotAnalysis);
  }

  @Test
  @DisplayName("It should update an existing swot analysis")
  void itShouldUpdateASwotAnalysis() {

    var swotDescription = "Swot Description";
    var layoutType = SwotLayoutTypes.DEFAULT.name();
    var swotTitle = "___________________11111111111111111__________";
    var swotId = "TEST1";
    UpdateSwotAnalysisDTO updateSwotAnalysisDTO = UpdateSwotAnalysisDTO.builder()
        .swotTemplate(true)
        .description(swotDescription)
        .layoutType(layoutType)
        .title(swotTitle)
        ._id(swotId)
        .build();

    SwotAnalysis mockedSwotAnalysis = GenerateSwotAnalysis.generateSwotAnalysis(currentLoggedUser.getUserId(), swotId);

    when(WebClientUtils.getUserHeadersInfo(any())).thenReturn(currentLoggedUser);

    when(swotAnalysisRepository.findBy_idAndOwnerId(anyString(), anyLong()))
        .thenReturn(Optional.of(mockedSwotAnalysis));

    when(swotAnalysisRepository.save(mockedSwotAnalysis))
        .thenReturn(mockedSwotAnalysis);

    SwotAnalysis updatedSwotAnalysis = underTest.update(swotId, updateSwotAnalysisDTO);
    verify(swotAnalysisRepository, times(1)).save(any());
    assertEquals(updateSwotAnalysisDTO.getTitle(), updatedSwotAnalysis.getTitle());

  }
}
