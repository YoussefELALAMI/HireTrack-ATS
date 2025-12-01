package com.hiretrack.backend.dto.auth;

import com.hiretrack.backend.entity.User;

public record AuthResponse(String accessToken, String tokenType, User user) {
}
