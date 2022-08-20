package com.microservices.swotlyzer.auth.service.services.impl;

import com.microservices.swotlyzer.auth.service.dtos.CreateUserDTO;
import com.microservices.swotlyzer.auth.service.dtos.LoginRequest;
import com.microservices.swotlyzer.auth.service.dtos.LoginResponse;
import com.microservices.swotlyzer.auth.service.models.Token;
import com.microservices.swotlyzer.auth.service.models.User;
import com.microservices.swotlyzer.auth.service.repositories.UserRepository;
import com.microservices.swotlyzer.auth.service.services.TokenProvider;
import com.microservices.swotlyzer.auth.service.utils.CookieUtil;
import org.apache.commons.lang.time.DateUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;
import web.error.handling.BadRequestException;
import web.error.handling.EntityExistsException;
import web.error.handling.ResourceNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    public static final String WANNABE_ACCESS_TOKEN = "WANNABE_ACCESS_TOKEN";
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
    @Mock
    private PasswordEncoder passwordEncoder;

    private AutoCloseable autoCloseable;

    private UserServiceImpl underTest;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new UserServiceImpl(userRepository, tokenProvider, cookieUtil, webClientBuilder, httpServletRequest,
                passwordEncoder);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }


    @Test
    @Disabled
    @DisplayName("It should create an user.")
    void itShouldCreateAnUser() {
        //give
        String userMail = "testemail@gmail.com";
        String phone = "61993459845";
        String password = "testepassword";
        String testName = "Test";
        Long userId = 1L;
        User createdUser =
                User.builder().id(userId).name(testName).phone(phone).email(userMail).password(password).build();

        when(userRepository.save(any())).thenReturn(createdUser);

        underTest.create(new CreateUserDTO(createdUser.getEmail(), createdUser.getName(), createdUser.getPassword(),
                createdUser.getPhone()));
        verify(userRepository, Mockito.times(1)).save(userArgumentCaptor.capture());
        //assert that the user created in this method  is the same as to the one captured by userArgumentCaptor
        assertThat(userArgumentCaptor.getValue().getId()).isEqualTo(userId);
        assertThat(userArgumentCaptor.getValue().getEmail()).isEqualTo(userMail);
    }

    @Test
    @DisplayName("Will throw error when Email is already in use by another User.")
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
        assertThatThrownBy(() -> underTest.create(
                new CreateUserDTO(createdUser.getEmail(), createdUser.getName(), createdUser.getPassword(),
                        createdUser.getPhone()))).isInstanceOf(EntityExistsException.class)
                .hasMessageContaining("User with email: " + userMail + " already exists.");

        verify(userRepository, never()).save(Mockito.any());

    }

    @Test
    @DisplayName("It should login the user successfully.")
    void itShouldLoginUser() {
        //given
        LoginRequest loginRequest = new LoginRequest("teste@mail.com", "123456");
        String userMail = "testemail@gmail.com";
        String phone = "61993459845";
        String password = "testepassword";
        String testName = "Test";
        Long userId = 1L;
        User userToLogin =
                User.builder().id(userId).name(testName).phone(phone).email(userMail).password(password).build();

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(userToLogin));
        var tokenMock = new Token(Token.TokenType.ACCESS, WANNABE_ACCESS_TOKEN, DateUtils.MILLIS_PER_DAY, null);
        when(tokenProvider.generateAccessToken(anyString())).thenReturn(tokenMock);
        when(tokenProvider.generateRefreshToken(anyString())).thenReturn(tokenMock);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(cookieUtil.createAccessTokenCookie(anyString(), anyLong())).thenReturn(
                ResponseCookie.from("accessToken", WANNABE_ACCESS_TOKEN).build());

        when(cookieUtil.createRefreshTokenCookie(anyString(), anyLong())).thenReturn(
                ResponseCookie.from("refreshToken", WANNABE_ACCESS_TOKEN).build());

        var response = underTest.login(loginRequest, null, null);


        assertThat(response.getHeaders().get(HttpHeaders.SET_COOKIE)).isNotNull();
        var responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(LoginResponse.SuccessFailure.SUCCESS);
    }

    @Test
    @DisplayName("Will throw error when login credentials are wrong.")
    void willThrowWhenLoginCredentialsMismatch() {
        LoginRequest loginRequest = new LoginRequest("teste@mail.com", "123456");
        String userMail = "testemail@gmail.com";
        String phone = "61993459845";
        String password = "testepassword";
        String testName = "Test";
        Long userId = 1L;
        User userToLogin =
                User.builder().id(userId).name(testName).phone(phone).email(userMail).password(password).build();

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(userToLogin));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        assertThatThrownBy(() -> underTest.login(loginRequest, null, null)).isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Password doesn't match!");
        verify(tokenProvider, never()).generateAccessToken(Mockito.any());
        verify(tokenProvider, never()).generateRefreshToken(Mockito.any());

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
    @DisplayName("It should return an user by Id.")
    void itShouldFindUserById() {
        String userMail = "testemail@gmail.com";
        String phone = "61993459845";
        String password = "123456";
        String testName = "Test";
        Long userId = 1L;
        User user = User.builder().id(userId).name(testName).phone(phone).email(userMail).password(password).build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        var userById = underTest.findById(user.getId());

        assertThat(userById.getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("Will throw error when user Id doesn't exist.")
    void willThrowWhenUserIdDontExist() {
        assertThatThrownBy(() -> underTest.findById(anyLong())).isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found.");
    }

    @Test
    @DisplayName("It should get the current logged user.")
    void itShouldGetCurrentLoggedUser() {
        //given
        String userMail = "testemail@gmail.com";
        String phone = "61993459845";
        String password = "123456";
        String testName = "Test";
        Long userId = 1L;
        User currentUser =
                User.builder().id(userId).name(testName).phone(phone).email(userMail).password(password).build();

        when(httpServletRequest.getHeader(anyString())).thenReturn(userId.toString());

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(currentUser));

        var loggedUser = underTest.me();

        assertThat(loggedUser.getId()).isEqualTo(userId);

    }

    @Test
    @DisplayName("Will throw error when user Id header are not present.")
    void willThrowWhenUserIdHeaderIsEmpty() {
        when(httpServletRequest.getHeader(anyString())).thenReturn(null);

        assertThatThrownBy(() -> underTest.me()).isInstanceOf(BadRequestException.class)
                .hasMessageContaining("No user ID headers found!");


        verify(userRepository, never()).findById(Mockito.anyLong());

    }
}