package com.api.swotlyzer.services.impl;

import com.api.swotlyzer.dtos.CreateUserDTO;
import com.api.swotlyzer.exceptions.EntityExistsException;
import com.api.swotlyzer.exceptions.ResourceNotFoundException;
import com.api.swotlyzer.models.User;
import com.api.swotlyzer.repositories.UsersRepository;
import com.api.swotlyzer.services.UsersService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

@Service
public class UsersServiceImpl implements UsersService {
    @Autowired
    private UsersRepository usersRepository;


    @Override
    public User create(CreateUserDTO userDTO) {
        Optional<User> userOptional = this.usersRepository.findUserByEmail(userDTO.getEmail());
        if (userOptional.isPresent())
            throw new EntityExistsException("User with email: " + userDTO.getEmail() + " already exists.");
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10, new SecureRandom());
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return this.usersRepository.save(user);
    }

    @Override
    public User findById(String id) {
        return this.usersRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User not found."));
    }
}
