package com.api.swotlyzer.services.impl;

import com.api.swotlyzer.dtos.CustomUserDetails;
import com.api.swotlyzer.models.User;
import com.api.swotlyzer.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @Author: TCMALTUNKAN - MEHMET ANIL ALTUNKAN
 * @Date: 30.12.2019:09:07, Pzt
 **/
@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UsersRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user =
                userRepository.findUserByEmail(s).orElseThrow(() -> new UsernameNotFoundException("User not found with email " + s));
        return new CustomUserDetails(user);
    }
}
