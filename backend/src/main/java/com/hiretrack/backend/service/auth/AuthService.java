package com.hiretrack.backend.service.auth;

import com.hiretrack.backend.dto.auth.AuthResponse;
import com.hiretrack.backend.dto.auth.LoginRequest;
import com.hiretrack.backend.dto.auth.RegisterRequest;
import com.hiretrack.backend.entity.User;
import com.hiretrack.backend.enums.Role;
import com.hiretrack.backend.repository.UserRepository;
import com.hiretrack.backend.security.jwt.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final Role role;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtTokenProvider tokenProvider,
            Role role
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = tokenProvider;
        this.role = role;
    }

    /**
     * Handle user registration :
     * creating a new user account in your system safely and correctly
     * @param request represents the data sent by the client when registering a new user.
     */
    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UsernameNotFoundException("Email already in use");
        }
        
        // Convert role string to Role enum, or use default role if invalid
        Role userRole;
        try {
            userRole = Role.valueOf(request.role().toUpperCase());
        } catch (IllegalArgumentException e) {
            // If role is invalid, use the default role from configuration
            userRole = this.role;
        }
        
        User user = new User();
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setRole(userRole);
        user.setCompanyName(request.companyName());
        user.setPhoneNumber(request.phoneNumber());
        user.setCreatedAt(LocalDateTime.now());
        
        userRepository.save(user); // persist entity to the database.
    }

   /**
     * Authenticate the user using Spring Security and issue a JWT.
     * Steps:
     * 1. Build an authentication token with the provided username/password.
     * 2. Delegate authentication to the configured AuthenticationManager.
     * 3. Obtain the authenticated principal as UserDetails.
     * 4. Generate a JWT for the authenticated user and return it inside an AuthResponse.
     * Throws AuthenticationException when credentials are invalid.
     *
     * @param request the login request containing username and password
     * @return AuthResponse containing the JWT and the token type ("Bearer")
     */
    public AuthResponse login(LoginRequest request){

        // Build an authentication token from the incoming credentials.
        // UsernamePasswordAuthenticationToken is the standard implementation
        // used by Spring Security for username/password authentication.
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(request.email(), request.password());

        // Perform authentication. This will consult the configured AuthenticationProvider(s).
        // If authentication fails an AuthenticationException will be thrown.
        Authentication authentication = authenticationManager.authenticate(authToken);

        // After successful authentication, retrieve the authenticated principal.
        // Our JpaUserDetailsService returns a Spring Security UserDetails implementation,
        // not the domain User entity, so we must look up the User entity separately.
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Look up the corresponding User entity by email (username in UserDetails).
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Generate a JWT for the authenticated user. The JwtTokenProvider is responsible
        // for creating a signed token that encodes the user's identity and any claims.
        String token = jwtTokenProvider.generateToken(userDetails);

        // Return the token with the standard Bearer scheme used in Authorization headers.
        return new AuthResponse(token, "Bearer", user);
    }
}
