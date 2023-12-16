package com.safroalex.AutoServiceERP.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safroalex.AutoServiceERP.controller.AuthController;
import com.safroalex.AutoServiceERP.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testAuthenticateUser() throws Exception {
        String username = "user";
        String password = "password";
        AuthController.LoginRequest loginRequest = new AuthController.LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        when(userService.authenticate(username, password)).thenReturn(true);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("User authenticated successfully"));

        verify(userService, times(1)).authenticate(username, password);
    }

    @Test
    void testRegisterUser() throws Exception {
        String username = "newuser";
        String password = "newpassword";
        AuthController.RegistrationRequest registrationRequest = new AuthController.RegistrationRequest();
        registrationRequest.setUsername(username);
        registrationRequest.setPassword(password);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("User registered successfully"));

        verify(userService, times(1)).registerUser(username, password);
    }
}
