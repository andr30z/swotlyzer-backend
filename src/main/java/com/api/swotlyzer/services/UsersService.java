package com.api.swotlyzer.services;

import com.api.swotlyzer.dtos.CreateUserDTO;
import com.api.swotlyzer.models.UsersModel;

public interface UsersService {
    UsersModel create(CreateUserDTO userDTO);
}
