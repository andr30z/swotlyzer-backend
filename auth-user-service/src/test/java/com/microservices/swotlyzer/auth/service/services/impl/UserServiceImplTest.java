package com.microservices.swotlyzer.auth.service.services.impl;

import com.microservices.swotlyzer.auth.service.dtos.CreateUserDTO;
import com.microservices.swotlyzer.auth.service.models.User;
import com.microservices.swotlyzer.auth.service.repositories.UserRepository;
import com.microservices.swotlyzer.auth.service.services.TokenProvider;
import com.microservices.swotlyzer.auth.service.utils.CookieUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import web.error.handling.EntityExistsException;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenProvider tokenProvider;
    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private WebClient.Builder webClientBuilder;
    @Mock
    private CookieUtil cookieUtil;

    private AutoCloseable autoCloseable;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        userService =
                new UserServiceImpl(userRepository, tokenProvider, cookieUtil, webClientBuilder, httpServletRequest);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }


    @Test
    @Disabled
    @DisplayName("It Should create an user.")
    void itShouldCreateAnUser() {
        //give
        String userMail = "testemail@gmail.com";
        String phone = "61993459845";
        String password = "testepassword";
        String testName = "Test";
        Long userId = 1L;
        User createdUser =
                User.builder().id(userId).name(testName).phone(phone).email(userMail).password(password).build();

        Mockito.when(userRepository.save(Mockito.any())).thenReturn(createdUser);

        userService.create(new CreateUserDTO(createdUser.getEmail(), createdUser.getName(), createdUser.getPassword(),
                createdUser.getPhone()));
        Mockito.verify(userRepository, Mockito.times(1)).save(userArgumentCaptor.capture());
        System.out.println(userArgumentCaptor.getValue());
        //assert that the user created in this method  is the same as to the one captured by userArgumentCaptor
        Assertions.assertThat(userArgumentCaptor.getValue().getId()).isEqualTo(userId);
        Assertions.assertThat(userArgumentCaptor.getValue().getEmail()).isEqualTo(userMail);
    }

    @Test
    void willThrowWhenEmailIsTaken() {
        // given
        String userMail = "testemail@gmail.com";
        String phone = "61993459845";
        String password = "testepassword";
        String testName = "Test";
        Long userId = 1L;
        User createdUser =
                User.builder().id(userId).name(testName).phone(phone).email(userMail).password(password).build();

        BDDMockito.given(userRepository.findUserByEmail(Mockito.anyString())).willReturn(Optional.of(createdUser));

        // when
        // then
        Assertions.assertThatThrownBy(() -> userService.create(
                        new CreateUserDTO(createdUser.getEmail(), createdUser.getName(), createdUser.getPassword(),
                                createdUser.getPhone()))).isInstanceOf(EntityExistsException.class)
                .hasMessageContaining("User with email: " + userMail + " already exists.");

        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());

    }

    @Test
    @Disabled
    void login() {
    }

    @Test
    @Disabled
    void refresh() {
    }

    @Test
    @Disabled
    void getTokenUser() {
    }

    @Test
    @Disabled
    void findById() {
    }

    @Test
    @Disabled
    void me() {
    }
}