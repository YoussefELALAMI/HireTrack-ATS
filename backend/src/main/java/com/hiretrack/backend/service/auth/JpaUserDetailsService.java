package com.hiretrack.backend.service.auth;

import com.hiretrack.backend.entity.User;
import com.hiretrack.backend.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JpaUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads a user by their email address and adapts the application's {@link User} entity
     * to Spring Security's {@link UserDetails} contract.
     * <p>
     * This method performs three main steps:
     * <li> 1. Fetch the User entity from the repository using the provided email.
     * <li> 2. Convert the user's role into a collection of {@link GrantedAuthority} instances
     * that Spring Security uses for authorization checks.
     * <li> 3. Build and return a Spring Security {@link org.springframework.security.core.userdetails.User}
     * which implements {@link UserDetails}.
     *
     * @param email the email (used as username) to look up
     * @return a {@link UserDetails} representing the found user
     * @throws UsernameNotFoundException if no user exists with the given email
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1) Retrieve user by email from repository. If not present, throw UsernameNotFoundException
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // 2) Map the domain Role enum to Spring Security GrantedAuthority.
        //    We store roles as enum values on the User entity; use the enum name as the authority string.
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(user.getRole().name())
        );

        // 3) Return a Spring Security UserDetails instance with email, password hash, and authorities.
        //    The User constructor used here is from org.springframework.security.core.userdetails and
        //    contains additional fields (enabled, accountNonExpired, etc.) that can be supplied if needed.
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPasswordHash(), authorities);
    }
}
