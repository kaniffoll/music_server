package com.kanifol.musicserver.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtService {

    @Value("${musicserver.app.jwt.secret}")
    private String jwtSecret;
    @Value("${musicserver.app.jwt.access-exp}")
    private long accessTokenExp;
    @Value("${musicserver.app.jwt.refresh-exp}")
    private long refreshTokenExp;

    private String generateJwtToken(Authentication authentication, long exp, String tokenType) {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        String username = authentication.getName();
        return Jwts.builder()
                .header()
                    .add("tokenType", tokenType)
                    .and()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + exp))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(Authentication authentication) {
        return generateJwtToken(authentication, refreshTokenExp, "refresh");
    }

    public String generateAccessToken(Authentication authentication) {
        return generateJwtToken(authentication, accessTokenExp, "access");
    }
}
