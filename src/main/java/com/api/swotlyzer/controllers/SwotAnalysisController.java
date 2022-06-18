package com.api.swotlyzer.controllers;

import com.api.swotlyzer.dtos.CreateSwotAnalysisDTO;
import com.api.swotlyzer.dtos.PaginationResponse;
import com.api.swotlyzer.dtos.UpdateSwotAnalysisDTO;
import com.api.swotlyzer.models.SwotAnalysis;
import com.api.swotlyzer.services.SwotAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/v1/swot-analysis")
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
