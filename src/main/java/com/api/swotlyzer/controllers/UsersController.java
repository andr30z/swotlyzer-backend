package com.api.swotlyzer.controllers;

import com.api.swotlyzer.dtos.CreateUserDTO;
import com.api.swotlyzer.models.User;
import com.api.swotlyzer.services.UsersService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/v1/users")
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }


    @GetMapping
    public String HelloWorld() {
        return "Hello World!";
    }

    @PostMapping
    public User create(@RequestBody @Validated CreateUserDTO createUserDTO) {
        return this.usersService.create(createUserDTO);
    }

    @GetMapping("/me")
    public User me() {
        return this.usersService.me();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable String id) {
        return usersService.findById(id);
    }
}
