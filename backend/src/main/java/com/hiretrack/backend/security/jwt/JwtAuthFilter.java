package com.hiretrack.backend.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    /**
     * Constructor for JwtAuthFilter.
     *
     * @param jwtTokenProvider utility responsible for extracting data from and validating JWT tokens
     * @param userDetailsService service used to load user details by username/email
     */
    public JwtAuthFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Filter that runs once per request. It:
     * <li> 1. Reads the Authorization header.
     * <li> 2. Extracts the Bearer token if present.
     * <li> 3. Extracts the username/email from the token.
     * <li> 4. Loads UserDetails and validates the token against them.
     * <li> 5. If valid, creates an Authentication and stores it in the SecurityContext.
     *
     * <p> This ensures downstream code (controllers, services) can access the authenticated principal. </p>
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Read the Authorization header
        String header = request.getHeader("Authorization");

        // Proceed only if header contains a Bearer token
        if (header != null && header.startsWith("Bearer ")) {
            // Remove "Bearer " prefix to obtain the raw JWT
            String token = header.substring(7);

            // Extract username/email from token. This should not perform heavy operations.
            String username = jwtTokenProvider.extractEmail(token);

            // Only attempt authentication if we extracted a username and there is no existing authentication
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Load user details (authorities, password, etc.) required for building the Authentication object
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Validate the token against the loaded user details (checks signature, expiry, and optionally user data)
                if (jwtTokenProvider.isTokenValid(token, userDetails)) {
                    // Build an Authentication token (principal, credentials, authorities)
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    // Store authentication in the SecurityContext so Spring Security is aware of the authenticated user
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        // Continue the filter chain regardless of authentication outcome
        filterChain.doFilter(request, response);
    }
}