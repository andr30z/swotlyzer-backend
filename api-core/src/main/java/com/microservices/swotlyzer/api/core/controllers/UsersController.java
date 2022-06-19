package com.microservices.swotlyzer.api.core.controllers;


import com.microservices.swotlyzer.api.core.models.User;
import com.microservices.swotlyzer.api.core.dtos.CreateUserDTO;
import com.microservices.swotlyzer.api.core.services.UsersService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/v1/users")
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
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
