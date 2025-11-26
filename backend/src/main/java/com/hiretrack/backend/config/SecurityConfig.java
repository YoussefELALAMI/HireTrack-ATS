package com.hiretrack.backend.config;

import com.hiretrack.backend.security.jwt.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity()
public class SecurityConfig {

    /**
     * <p> JWT authentication filter that validates tokens on incoming requests.
     * <p> Registered in the filter chain so it runs before Spring Security's
     * <p> UsernamePasswordAuthenticationFilter.
     */
    private final JwtAuthFilter jwtAuthFilter;

    /**
     * <p> Application's UserDetailsService used by DaoAuthenticationProvider
     * to load user-specific data during authentication.
     */
    private final UserDetailsService userDetailsService;

    /**
     * Constructor injection of required security components.
     *
     * @param jwtAuthFilter      filter that handles JWT token parsing and validation
     * @param userDetailsService service used to load user details for authentication
     */
    public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserDetailsService userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Configures the HTTP security for the application.
     *
     * <li> Disables CSRF since the API is stateless and typically used with tokens.
     * <li> Sets session management to STATELESS because JWTs are used instead of HTTP sessions.
     * <li> Allows unauthenticated access to endpoints under /api/auth/** (login, register, refresh).
     * <li> Requires authentication for any other request.
     * <li> Registers the application's UserDetailsService so authentication providers can use it.
     * <li> Adds the JwtAuthFilter before UsernamePasswordAuthenticationFilter to validate tokens early.
     *
     * @param http the HttpSecurity to configure
     * @return built SecurityFilterChain
     * @throws Exception if an error occurs while building the chain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                // ensure Spring Security can resolve UserDetails for authentication flows
                .userDetailsService(userDetailsService)
                // place JWT filter before username/password processing
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * <p> Exposes an AuthenticationManager bean wired with a DaoAuthenticationProvider.
     *
     * <p> The DaoAuthenticationProvider is configured with the provided PasswordEncoder
     * <p> and UserDetailsService. The ProviderManager wraps this provider and is returned
     * <p> as the AuthenticationManager to be used wherever injection is required.
     *
     * @param http               (not used directly here but kept for compatibility)
     * @param passwordEncoder    password encoder used to verify stored password hashes
     * @param userDetailsService service that loads user information
     * @return configured AuthenticationManager
     * @throws Exception if provider configuration fails
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       PasswordEncoder passwordEncoder,
                                                       UserDetailsService userDetailsService) throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // use the application's PasswordEncoder to validate credentials
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);

        // ProviderManager holds our configured provider(s)
        return new ProviderManager(provider);
    }

    /**
     * <p> Defines the PasswordEncoder bean used across the application.
     * <p> BCrypt is chosen for a good balance between security and performance.
     *
     * @return a BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
