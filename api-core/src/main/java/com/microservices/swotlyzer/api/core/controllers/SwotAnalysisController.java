package com.microservices.swotlyzer.api.core.controllers;


import com.microservices.swotlyzer.api.core.dtos.CreateSwotAnalysisDTO;
import com.microservices.swotlyzer.api.core.dtos.PaginationResponse;
import com.microservices.swotlyzer.api.core.dtos.UpdateSwotAnalysisDTO;
import com.microservices.swotlyzer.api.core.models.SwotAnalysis;
import com.microservices.swotlyzer.api.core.services.SwotAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/swot/api/v1/swot-analysis")
public class SwotAnalysisController {
    @Autowired
    private SwotAnalysisService swotAnalysisService;

    @GetMapping("{id}")
    public SwotAnalysis findById(@PathVariable String id) {
        return this.swotAnalysisService.findById(id);
    }

    @GetMapping("/my")
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
}
