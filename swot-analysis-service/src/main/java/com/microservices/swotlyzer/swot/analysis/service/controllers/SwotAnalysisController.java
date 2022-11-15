package com.microservices.swotlyzer.swot.analysis.service.controllers;

import com.microservices.swotlyzer.swot.analysis.service.dtos.*;
import com.microservices.swotlyzer.swot.analysis.service.models.SwotAnalysis;
import com.microservices.swotlyzer.swot.analysis.service.services.SwotAnalysisService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/v1/swot-analysis")
public class SwotAnalysisController {

  private final SwotAnalysisService swotAnalysisService;

  public SwotAnalysisController(SwotAnalysisService swotAnalysisService) {
    this.swotAnalysisService = swotAnalysisService;
  }

  @GetMapping("/me")
  public PaginationResponse<SwotAnalysis> getMySwotAnalyses(
    @RequestParam(name = "page", defaultValue = "1") int page,
    @RequestParam(name = "perPage", defaultValue = "15") int perPage
  ) {
    return this.swotAnalysisService.findByCurrentUser(page, perPage);
  }

  @GetMapping("/me/{id}")
  public SwotAnalysis getMySwotAnalysis(@PathVariable String swotId) {
    return this.swotAnalysisService.getSwotAnalysisByCurrentUser(swotId);
  }

  @PostMapping
  public SwotAnalysis create(
    @Validated @RequestBody CreateSwotAnalysisDTO createSWOTAnalysisDTO
  ) {
    return this.swotAnalysisService.create(createSWOTAnalysisDTO);
  }

  @PutMapping("/{id}")
  public SwotAnalysis update(
    @PathVariable String id,
    @Validated @RequestBody UpdateSwotAnalysisDTO updateSWOTAnalysisDTO
  ) {
    return this.swotAnalysisService.update(id, updateSWOTAnalysisDTO);
  }

  @DeleteMapping("/{id}")
  public SuccessDeleteSwotAnalysisDTO deleteById(@PathVariable String id) {
    return this.swotAnalysisService.deleteSwotAnalysisByCurrentUser(id);
  }
}
