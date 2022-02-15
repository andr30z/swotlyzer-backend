package com.api.swotlyzer.controllers;

import com.api.swotlyzer.dtos.CreateUserDTO;
import com.api.swotlyzer.models.UsersModel;
import com.api.swotlyzer.services.UsersService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UsersController {

    final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }


    @GetMapping
    public String HelloWorld() {
        return "Hello World!";
    }

    @PostMapping
    public UsersModel create(@RequestBody @Validated CreateUserDTO createUserDTO) {
        return usersService.create(createUserDTO);
    }
}
