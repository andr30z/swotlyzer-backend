package com.swotlyzer.api.core.controllers;

import com.swotlyzer.api.core.dtos.CreateSwotFieldDTO;
import com.swotlyzer.api.core.models.SwotField;
import com.swotlyzer.api.core.services.SwotFieldService;
import com.swotlyzer.api.core.dtos.SwotFieldDeleteResponse;
import com.swotlyzer.api.core.dtos.UpdateSwotFieldDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/v1/swot-field")
public class SwotFieldController {
    @Autowired
    private SwotFieldService swotFieldService;

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