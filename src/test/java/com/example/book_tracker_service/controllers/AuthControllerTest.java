package com.example.book_tracker_service.controllers;

import com.example.book_tracker_service.models.AuthToken;
import com.example.book_tracker_service.models.User;
import com.example.book_tracker_service.repo.AuthTokenRepository;
import com.example.book_tracker_service.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthTokenRepository authTokenRepository;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;
    private User user;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();

        user = new User();
        user.setId(1L);
        user.setName("testUser");
        user.setPassword("encodedPassword");
    }

    @Test
    void testLogin_Success() throws Exception {

        when(userService.userByName(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testUser\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is(notNullValue())));

        verify(authTokenRepository, times(1)).save(any(AuthToken.class));
    }

    @Test
    void testLogin_UserNotFound() throws Exception {

        when(userService.userByName(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"unknownUser\",\"password\":\"password\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Incorrect login or password"));

        verify(authTokenRepository, times(0)).save(any(AuthToken.class));
    }

    @Test
    void testLogin_IncorrectPassword() throws Exception {

        when(userService.userByName(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testUser\",\"password\":\"wrongPassword\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Incorrect login or password"));

        verify(authTokenRepository, times(0)).save(any(AuthToken.class));
    }

    @Test
    void testSignup_Success() throws Exception {

        when(userService.userByName(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"newUser\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Successful registration"));

        verify(userService, times(1)).addUser(any(User.class));
    }

    @Test
    void testSignup_UserAlreadyExists() throws Exception {

        when(userService.userByName(anyString())).thenReturn(Optional.of(user));

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testUser\",\"password\":\"password\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User with name testUser already exists"));

        verify(userService, times(0)).addUser(any(User.class));
    }

    @Test
    void testLogout_Success() throws Exception {
        AuthToken authToken = new AuthToken();
        authToken.setId(1L);
        authToken.setToken("validToken");

        when(authTokenRepository.findByToken(anyString())).thenReturn(Optional.of(authToken));

        mockMvc.perform(post("/auth/logout")
                        .header("Authorization", "Bearer validToken"))
                .andExpect(status().isOk())
                .andExpect(content().string("Logged out"));

        verify(authTokenRepository, times(1)).deleteById(authToken.getId());
    }

    @Test
    void testLogout_Unauthorized() throws Exception {
        mockMvc.perform(post("/auth/logout"))
                .andExpect(status().isUnauthorized());

        verify(authTokenRepository, times(0)).deleteById(anyLong());
    }
}
