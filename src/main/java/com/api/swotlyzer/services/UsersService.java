package com.api.swotlyzer.services;

import com.api.swotlyzer.dtos.CreateUserDTO;
import com.api.swotlyzer.models.User;

public interface UsersService {
    User create(CreateUserDTO userDTO);
    User findById(String id);
}
