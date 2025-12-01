package com.hiretrack.backend.service.auth;

import com.hiretrack.backend.dto.auth.AuthResponse;
import com.hiretrack.backend.dto.auth.LoginRequest;
import com.hiretrack.backend.dto.auth.RegisterRequest;
import com.hiretrack.backend.entity.User;
import com.hiretrack.backend.enums.Role;
import com.hiretrack.backend.repository.UserRepository;
import com.hiretrack.backend.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Unit Tests")
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserDetailsService userDetailsService;

    private Role defaultRole;

    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User savedUser;
    private String encodedPassword;
    private Authentication authentication;
    private UserDetails userDetails;
    private String jwtToken;

    @BeforeEach
    void setUp() {
        defaultRole = Role.RECRUITER;
        authService = new AuthService(
                userRepository,
                passwordEncoder,
                authenticationManager,
                jwtTokenProvider,
                defaultRole
        );

        registerRequest = new RegisterRequest(
                "test@example.com",
                "password123",
                "John",
                "Doe",
                "RECRUITER",
                "Test Company",
                "+1234567890"
        );
        loginRequest = new LoginRequest("test@example.com", "password123");
        encodedPassword = "$2a$10$encodedPasswordHash";
        jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test.token";

        savedUser = new User();
        savedUser.setEmail("test@example.com");
        savedUser.setPasswordHash(encodedPassword);
        savedUser.setRole(Role.RECRUITER);
        savedUser.setFirstName("John");
        savedUser.setLastName("Doe");
        savedUser.setCompanyName("Test Company");
        savedUser.setPhoneNumber("+1234567890");

        userDetails = org.springframework.security.core.userdetails.User.builder()
                .username("test@example.com")
                .password(encodedPassword)
                .authorities("RECRUITER")
                .build();

        authentication = mock(Authentication.class);
        // Removed: when(authentication.getPrincipal()).thenReturn(userDetails);
    }

    @Test
    @DisplayName("Should register a new user successfully with all fields")
    void testRegister_Success() {
        // Given
        when(userRepository.existsByEmail(registerRequest.email())).thenReturn(false);
        when(passwordEncoder.encode(registerRequest.password())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        authService.register(registerRequest);

        // Then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).existsByEmail(registerRequest.email());
        verify(passwordEncoder, times(1)).encode(registerRequest.password());
        verify(userRepository, times(1)).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        assertEquals(registerRequest.email(), capturedUser.getEmail());
        assertEquals(encodedPassword, capturedUser.getPasswordHash());
        assertEquals(registerRequest.firstName(), capturedUser.getFirstName());
        assertEquals(registerRequest.lastName(), capturedUser.getLastName());
        assertEquals(Role.RECRUITER, capturedUser.getRole());
        assertEquals(registerRequest.companyName(), capturedUser.getCompanyName());
        assertEquals(registerRequest.phoneNumber(), capturedUser.getPhoneNumber());
        assertNotNull(capturedUser.getCreatedAt());
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void testRegister_EmailAlreadyExists() {
        // Given
        when(userRepository.existsByEmail(registerRequest.email())).thenReturn(true);

        // When & Then
        assertThrows(org.springframework.security.core.userdetails.UsernameNotFoundException.class, () -> {
            authService.register(registerRequest);
        });

        verify(userRepository, times(1)).existsByEmail(registerRequest.email());
        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

   @Test
    @DisplayName("Should login successfully and return AuthResponse with token")
    void testLogin_Success() {
        // Given
        when(authentication.getPrincipal()).thenReturn(userDetails); // Moved here
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtTokenProvider.generateToken(userDetails)).thenReturn(jwtToken);
        when(userRepository.findByEmail(loginRequest.email())).thenReturn(java.util.Optional.of(savedUser));

        // When
        AuthResponse response = authService.login(loginRequest);

        // Then
        assertNotNull(response);
        assertEquals(jwtToken, response.accessToken());
        assertEquals("Bearer", response.tokenType());

        ArgumentCaptor<UsernamePasswordAuthenticationToken> tokenCaptor =
                ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
        verify(authenticationManager, times(1)).authenticate(tokenCaptor.capture());

        UsernamePasswordAuthenticationToken capturedToken = tokenCaptor.getValue();
        assertEquals(loginRequest.email(), capturedToken.getPrincipal());
        assertEquals(loginRequest.password(), capturedToken.getCredentials());

        verify(jwtTokenProvider, times(1)).generateToken(userDetails);
    }

    @Test
    @DisplayName("Should throw exception when authentication fails")
    void testLogin_AuthenticationFailure() {
        // Given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // When & Then
        assertThrows(BadCredentialsException.class, () -> {
            authService.login(loginRequest);
        });

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider, never()).generateToken(any(UserDetails.class));
    }

    @Test
    @DisplayName("Should handle null login request gracefully")
    void testLogin_NullRequest() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            authService.login(null);
        });
    }

    @Test
    @DisplayName("Should handle null register request gracefully")
    void testRegister_NullRequest() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            authService.register(null);
        });
    }

    @Test
    @DisplayName("Should encode password during registration")
    void testRegister_PasswordEncoding() {
        // Given
        String plainPassword = "plainPassword123";
        String expectedEncoded = "$2a$10$encodedHash";
        RegisterRequest request = new RegisterRequest(
                "newuser@example.com",
                plainPassword,
                "Jane",
                "Smith",
                "ADMIN",
                null,
                null
        );

        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(plainPassword)).thenReturn(expectedEncoded);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        authService.register(request);

        // Then
        verify(passwordEncoder, times(1)).encode(plainPassword);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertEquals(expectedEncoded, userCaptor.getValue().getPasswordHash());
    }

    @Test
    @DisplayName("Should register user with valid role from request")
    void testRegister_WithValidRole() {
        // Given
        RegisterRequest request = new RegisterRequest(
                "admin@example.com",
                "password123",
                "Admin",
                "User",
                "ADMIN",
                "Admin Corp",
                "+9876543210"
        );

        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        authService.register(request);

        // Then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        assertEquals(Role.ADMIN, capturedUser.getRole());
        assertEquals(request.firstName(), capturedUser.getFirstName());
        assertEquals(request.lastName(), capturedUser.getLastName());
    }

    @Test
    @DisplayName("Should use default role when invalid role is provided")
    void testRegister_WithInvalidRole() {
        // Given
        RegisterRequest request = new RegisterRequest(
                "user@example.com",
                "password123",
                "Test",
                "User",
                "INVALID_ROLE",
                null,
                null
        );

        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        authService.register(request);

        // Then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        // Should use default role (RECRUITER) when invalid role is provided
        assertEquals(defaultRole, capturedUser.getRole());
    }

    @Test
    @DisplayName("Should register user with optional fields as null")
    void testRegister_WithOptionalFieldsNull() {
        // Given
        RegisterRequest request = new RegisterRequest(
                "minimal@example.com",
                "password123",
                "Minimal",
                "User",
                "HIRING_MANAGER",
                null,
                null
        );

        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        authService.register(request);

        // Then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        assertNull(capturedUser.getCompanyName());
        assertNull(capturedUser.getPhoneNumber());
        assertEquals(Role.HIRING_MANAGER, capturedUser.getRole());
    }

    @Test
    @DisplayName("Should set createdAt timestamp during registration")
    void testRegister_SetsCreatedAtTimestamp() {
        // Given
        when(userRepository.existsByEmail(registerRequest.email())).thenReturn(false);
        when(passwordEncoder.encode(registerRequest.password())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        LocalDateTime beforeRegistration = LocalDateTime.now();

        // When
        authService.register(registerRequest);

        // Then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        
        LocalDateTime afterRegistration = LocalDateTime.now();
        assertNotNull(capturedUser.getCreatedAt());
        assertTrue(capturedUser.getCreatedAt().isAfter(beforeRegistration.minusSeconds(1)));
        assertTrue(capturedUser.getCreatedAt().isBefore(afterRegistration.plusSeconds(1)));
    }
}