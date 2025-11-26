package com.hiretrack.backend.config;

import com.hiretrack.backend.enums.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    /**
     * Provides the default role to be assigned to newly registered users.
     * The role value is configured via application.yml under app.default-role.
     *
     * @param roleString the role name from configuration (defaults to "RECRUITER")
     * @return the Role enum value
     */
    @Bean
    public Role defaultRole(@Value("${app.default-role:RECRUITER}") String roleString) {
        return Role.valueOf(roleString);
    }
}