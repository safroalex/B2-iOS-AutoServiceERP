package com.safroalex.AutoServiceERP.controller;

import com.safroalex.AutoServiceERP.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        boolean isAuthenticated = userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());

        if (isAuthenticated) {
            // Возвращаем успешный ответ, возможно с JWT или другой информацией
            return ResponseEntity.ok().body("User authenticated successfully");
        } else {
            // Возвращаем ошибку аутентификации
            return ResponseEntity.status(401).body("Authentication failed");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest registrationRequest) {
        try {
            userService.registerUser(registrationRequest.getUsername(), registrationRequest.getPassword());
            return ResponseEntity.ok().body("User registered successfully");
        } catch (RuntimeException e) {
            // Обработка ошибки, например, если пользователь уже существует
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Вспомогательный класс для получения данных запроса
    public static class LoginRequest {
        private String username;
        private String password;

        // Геттеры и сеттеры
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class RegistrationRequest {
        private String username;
        private String password;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
