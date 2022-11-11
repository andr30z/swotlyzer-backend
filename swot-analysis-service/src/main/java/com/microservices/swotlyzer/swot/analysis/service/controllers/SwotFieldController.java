package com.microservices.swotlyzer.swot.analysis.service.controllers;

import com.microservices.swotlyzer.swot.analysis.service.dtos.CreateSwotFieldDTO;
import com.microservices.swotlyzer.swot.analysis.service.dtos.SwotFieldDeleteResponse;
import com.microservices.swotlyzer.swot.analysis.service.dtos.UpdateSwotFieldDTO;
import com.microservices.swotlyzer.swot.analysis.service.models.SwotField;
import com.microservices.swotlyzer.swot.analysis.service.services.SwotFieldService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/v1/swot-analysis/swot-field")
public class SwotFieldController {

    private final SwotFieldService swotFieldService;

    public SwotFieldController(SwotFieldService swotFieldService) {
        this.swotFieldService = swotFieldService;
    }

    @GetMapping("{id}")
    public SwotField findById(@PathVariable String id) {
        return this.swotFieldService.findById(id);
    }

    @PostMapping
    public SwotField create(@Validated @RequestBody CreateSwotFieldDTO createSwotFieldDTO) {
        return this.swotFieldService.create(createSwotFieldDTO);
    }

    @PutMapping("/{id}")
    public SwotField update(@PathVariable String id, @Validated @RequestBody UpdateSwotFieldDTO updateSwotFieldDTO) {
     return this.swotFieldService.update(id, updateSwotFieldDTO);
    }

    @DeleteMapping("/{id}")
    public SwotFieldDeleteResponse delete(@PathVariable String id) {
        return this.swotFieldService.delete(id);
    }
}
