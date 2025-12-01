package com.hiretrack.backend.controller;

import com.hiretrack.backend.dto.auth.AuthResponse;
import com.hiretrack.backend.dto.auth.LoginRequest;
import com.hiretrack.backend.dto.auth.RegisterRequest;
import com.hiretrack.backend.service.auth.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
/**
 * REST controller that exposes authentication endpoints.
 *
 * Endpoints:
 *  - POST /api/auth/register : register a new user
 *  - POST /api/auth/login    : authenticate a user and return an auth payload
 *
 * This controller delegates business logic to the AuthService. It is kept thin:
 * controllers should handle HTTP request/response concerns while services contain
 * application logic and persistence interactions.
 */
public class AuthController {

    /**
     * Service responsible for authentication operations (register, login).
     * Injected via constructor (preferred for immutability and testability).
     */
    private final AuthService authService;

    /**
     * Constructor injection for AuthService.
     *
     * @param authService the authentication service bean provided by Spring
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Register a new user.
     *
     * Consumes a RegisterRequest payload from the request body and forwards it to the
     * AuthService. Returns HTTP 200 when registration succeeds, or HTTP 400 with a
     * simple error message when an exception occurs.
     *
     * Note: catching a broad Exception here keeps the controller simple for now,
     * but consider using more specific exceptions and a global @ControllerAdvice
     * for centralized error handling in larger applications.
     *
     * @param request registration details (username, password, etc.)
     * @return HTTP 200 and success message or HTTP 400 and error details
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        try {
            // Delegate registration logic to the service layer
            authService.register(request);
            // Return a simple success message on success
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            // Convert service error into a 400 Bad Request response with message
            return ResponseEntity.badRequest().body("Error registering user: " + e.getMessage());
        }
    }

    /**
     * Authenticate a user and return an AuthResponse.
     *
     * The LoginRequest (from the request body) is forwarded to AuthService.login,
     * which should validate credentials and create an authentication payload
     * (for example: a JWT token and user info). A successful login returns HTTP 200
     * with the AuthResponse body.
     *
     * @param request login credentials (username/email and password)
     * @return HTTP 200 with authentication response (tokens, user info, etc.)
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        // Perform authentication and build response via the service
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

}
