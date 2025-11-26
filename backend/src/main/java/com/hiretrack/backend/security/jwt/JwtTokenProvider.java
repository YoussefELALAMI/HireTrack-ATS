package com.hiretrack.backend.security.jwt;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.hiretrack.backend.config.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

    private final JwtProperties properties;
    private SecretKey cachedSigningKey;

    public JwtTokenProvider(JwtProperties properties) {
        this.properties = properties;
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(Map.of(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        Instant issuedAt = Instant.now();
        Instant expiration = issuedAt.plus(properties.getAccessTokenTtl());

        Map<String, Object> claims = new HashMap<>(Optional.ofNullable(extraClaims).orElse(Map.of()));

        return Jwts.builder()
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiration))
                .claims(claims)
                .subject(userDetails.getUsername())
                .signWith(getSigningKey())
                .compact();
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractEmail(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = parseClaims(token);
        return claimsResolver.apply(claims);
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        if (cachedSigningKey == null) {
            byte[] keyBytes = Decoders.BASE64.decode(properties.getSecret());
            cachedSigningKey = Keys.hmacShaKeyFor(keyBytes);
        }
        return cachedSigningKey;
    }
}


