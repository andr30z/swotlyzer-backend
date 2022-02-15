package com.api.swotlyzer.services.impl;

import com.api.swotlyzer.dtos.CreateUserDTO;
import com.api.swotlyzer.models.UsersModel;
import com.api.swotlyzer.repositories.UsersRepository;
import com.api.swotlyzer.services.UsersService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersServiceImpl implements UsersService {
    @Autowired
    UsersRepository usersRepository;


    @Override
    public UsersModel create(CreateUserDTO userDTO) {
        UsersModel user = new UsersModel();
        BeanUtils.copyProperties(userDTO, user);
        return this.usersRepository.save(user);
    }
}
