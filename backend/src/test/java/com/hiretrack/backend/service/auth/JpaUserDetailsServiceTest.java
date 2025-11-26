package com.hiretrack.backend.service.auth;

import com.hiretrack.backend.entity.User;
import com.hiretrack.backend.enums.Role;
import com.hiretrack.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JpaUserDetailsService Unit Tests")
class JpaUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JpaUserDetailsService jpaUserDetailsService;

    private User user;
    private String email;
    private String passwordHash;

    @BeforeEach
    void setUp() {
        email = "test@example.com";
        passwordHash = "$2a$10$encodedPasswordHash";

        user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordHash);
        user.setRole(Role.RECRUITER);
    }

    @Test
    @DisplayName("Should load user by username successfully")
    void testLoadUserByUsername_Success() {
        // Given
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // When
        UserDetails userDetails = jpaUserDetailsService.loadUserByUsername(email);

        // Then
        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals(passwordHash, userDetails.getPassword());
        assertFalse(userDetails.getAuthorities().isEmpty());
        
        GrantedAuthority authority = userDetails.getAuthorities().iterator().next();
        assertEquals(Role.RECRUITER.name(), authority.getAuthority());
        
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when user not found")
    void testLoadUserByUsername_UserNotFound() {
        // Given
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When & Then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            jpaUserDetailsService.loadUserByUsername(email);
        });

        assertEquals("User not found with email: " + email, exception.getMessage());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("Should load user with ADMIN role correctly")
    void testLoadUserByUsername_AdminRole() {
        // Given
        user.setRole(Role.ADMIN);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // When
        UserDetails userDetails = jpaUserDetailsService.loadUserByUsername(email);

        // Then
        assertNotNull(userDetails);
        GrantedAuthority authority = userDetails.getAuthorities().iterator().next();
        assertEquals(Role.ADMIN.name(), authority.getAuthority());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("Should load user with HIRING_MANAGER role correctly")
    void testLoadUserByUsername_HiringManagerRole() {
        // Given
        user.setRole(Role.HIRING_MANAGER);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // When
        UserDetails userDetails = jpaUserDetailsService.loadUserByUsername(email);

        // Then
        assertNotNull(userDetails);
        GrantedAuthority authority = userDetails.getAuthorities().iterator().next();
        assertEquals(Role.HIRING_MANAGER.name(), authority.getAuthority());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("Should load user with INTERVIEWER role correctly")
    void testLoadUserByUsername_InterviewerRole() {
        // Given
        user.setRole(Role.INTERVIEWER);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // When
        UserDetails userDetails = jpaUserDetailsService.loadUserByUsername(email);

        // Then
        assertNotNull(userDetails);
        GrantedAuthority authority = userDetails.getAuthorities().iterator().next();
        assertEquals(Role.INTERVIEWER.name(), authority.getAuthority());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("Should handle null email")
    void testLoadUserByUsername_NullEmail() {
        // Given
        when(userRepository.findByEmail(null)).thenReturn(Optional.empty());

        // When & Then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            jpaUserDetailsService.loadUserByUsername(null);
        });

        assertEquals("User not found with email: null", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(null);
    }

    @Test
    @DisplayName("Should handle empty email")
    void testLoadUserByUsername_EmptyEmail() {
        // Given
        String emptyEmail = "";
        when(userRepository.findByEmail(emptyEmail)).thenReturn(Optional.empty());

        // When & Then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            jpaUserDetailsService.loadUserByUsername(emptyEmail);
        });

        assertEquals("User not found with email: " + emptyEmail, exception.getMessage());
        verify(userRepository, times(1)).findByEmail(emptyEmail);
    }

    @Test
    @DisplayName("Should return UserDetails with correct account status")
    void testLoadUserByUsername_AccountStatus() {
        // Given
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // When
        UserDetails userDetails = jpaUserDetailsService.loadUserByUsername(email);

        // Then
        assertNotNull(userDetails);
        // Spring Security User is enabled by default
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
    }
}

