package com.hiretrack.backend.controller;

import com.hiretrack.backend.dto.auth.AuthResponse;
import com.hiretrack.backend.dto.auth.LoginRequest;
import com.hiretrack.backend.dto.auth.RegisterRequest;
import com.hiretrack.backend.entity.User;
import com.hiretrack.backend.enums.Role;
import com.hiretrack.backend.service.auth.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthController Unit Tests")
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private AuthResponse authResponse;
    private String jwtToken;

 @BeforeEach
 void setUp() {
     registerRequest = new RegisterRequest("test@example.com", "password123", "John", "Doe", "RECRUITER", "Test Company", "+1234567890");
     loginRequest = new LoginRequest("test@example.com", "password123");
     jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test.token";
     User user = new User();
     user.setEmail("test@example.com");
     user.setFirstName("John");
     user.setLastName("Doe");
     user.setRole(Role.valueOf("RECRUITER"));
     authResponse = new AuthResponse(jwtToken, "Bearer", user);
 }

    @Test
    @DisplayName("Should register user successfully and return 200 OK")
    void testRegister_Success() {
        // Given
        doNothing().when(authService).register(any(RegisterRequest.class));

        // When
        ResponseEntity<String> response = authController.register(registerRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody());
        verify(authService, times(1)).register(registerRequest);
    }

    @Test
    @DisplayName("Should return 400 Bad Request when email already exists")
    void testRegister_EmailAlreadyExists() {
        // Given
        doThrow(new UsernameNotFoundException("Email already in use"))
                .when(authService).register(any(RegisterRequest.class));

        // When
        ResponseEntity<String> response = authController.register(registerRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        String responseBody = response.getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.contains("Error registering user"));
        assertTrue(responseBody.contains("Email already in use"));
        verify(authService, times(1)).register(registerRequest);
    }

    @Test
    @DisplayName("Should return 400 Bad Request when registration throws exception")
    void testRegister_GenericException() {
        // Given
        String errorMessage = "Database connection failed";
        doThrow(new RuntimeException(errorMessage))
                .when(authService).register(any(RegisterRequest.class));

        // When
        ResponseEntity<String> response = authController.register(registerRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        String responseBody = response.getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.contains("Error registering user"));
        assertTrue(responseBody.contains(errorMessage));
        verify(authService, times(1)).register(registerRequest);
    }

    @Test
    @DisplayName("Should login successfully and return 200 OK with AuthResponse")
    void testLogin_Success() {
        // Given
        when(authService.login(any(LoginRequest.class))).thenReturn(authResponse);

        // When
        ResponseEntity<AuthResponse> response = authController.login(loginRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        AuthResponse responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(jwtToken, responseBody.accessToken());
        assertEquals("Bearer", responseBody.tokenType());
        verify(authService, times(1)).login(loginRequest);
    }

    @Test
    @DisplayName("Should handle null login request")
    void testLogin_NullRequest() {
        // Given
        when(authService.login(null)).thenThrow(new NullPointerException("Request cannot be null"));

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            authController.login(null);
        });

        verify(authService, times(1)).login(null);
    }

    @Test
    @DisplayName("Should handle authentication failure during login")
    void testLogin_AuthenticationFailure() {
        // Given
        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new org.springframework.security.authentication.BadCredentialsException("Invalid credentials"));

        // When & Then
        assertThrows(org.springframework.security.authentication.BadCredentialsException.class, () -> {
            authController.login(loginRequest);
        });

        verify(authService, times(1)).login(loginRequest);
    }

    @Test
    @DisplayName("Should handle null register request")
    void testRegister_NullRequest() {
        // Given
        doThrow(new NullPointerException("Request cannot be null"))
                .when(authService).register(null);

        // When
        ResponseEntity<String> response = authController.register(null);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        String responseBody = response.getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.contains("Error registering user"));
        verify(authService, times(1)).register(null);
    }
}

