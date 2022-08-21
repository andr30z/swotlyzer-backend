package com.microservices.swotlyzer.auth.service.repositories;

import com.microservices.swotlyzer.auth.service.models.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.mock.web.MockHttpServletRequest;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

//    @Bean
//    public MockHttpServletRequest httpServletRequest(){
//        return new MockHttpServletRequest();
//    }

    @Test
    void itShouldFindUserByEmail() {
        //give
        String userMail = "testemail@gmail.com";
        User user = User.builder().name("Test").password("testepassword").phone("61993459845")
                .email(userMail).build();
        userRepository.save(user);

        //when
        var userByEmail = userRepository.findUserByEmail(userMail);

        //then
        Assertions.assertThat(userByEmail).isNotEmpty();
    }
}