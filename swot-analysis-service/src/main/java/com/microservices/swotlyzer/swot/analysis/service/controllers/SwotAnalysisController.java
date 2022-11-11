package com.microservices.swotlyzer.swot.analysis.service.controllers;


import com.microservices.swotlyzer.api.core.dtos.*;
import com.microservices.swotlyzer.swot.analysis.service.dtos.CreateSwotAnalysisDTO;
import com.microservices.swotlyzer.swot.analysis.service.dtos.PaginationResponse;
import com.microservices.swotlyzer.swot.analysis.service.dtos.SuccessDeleteSwotAnalysisDTO;
import com.microservices.swotlyzer.swot.analysis.service.dtos.UpdateSwotAnalysisDTO;
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


    @GetMapping("{id}")
    public SwotAnalysis findById(@PathVariable String id) {
        return this.swotAnalysisService.findById(id);
    }

    @GetMapping("/me")
    public PaginationResponse<SwotAnalysis> getMySwotAnalysis(@RequestParam(name = "page", defaultValue = "1") int page,
                                                              @RequestParam(name = "perPage", defaultValue = "15")
                                                                      int perPage) {
        return this.swotAnalysisService.findByCurrentUser(page, perPage);
    }

    @PostMapping
    public SwotAnalysis create(@Validated @RequestBody CreateSwotAnalysisDTO createSWOTAnalysisDTO) {
        return this.swotAnalysisService.create(createSWOTAnalysisDTO);
    }

    @PutMapping("/{id}")
    public SwotAnalysis update(@PathVariable String id,
                               @Validated @RequestBody UpdateSwotAnalysisDTO updateSWOTAnalysisDTO) {
        return this.swotAnalysisService.update(id, updateSWOTAnalysisDTO);
    }

    @DeleteMapping("/{id}")
    public SuccessDeleteSwotAnalysisDTO deleteById(@PathVariable String id) {
        return this.swotAnalysisService.deleteSwotAnalysisByCurrentUser(id);
    }
}
