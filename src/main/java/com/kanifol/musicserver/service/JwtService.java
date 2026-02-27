package com.kanifol.musicserver.service;

import com.kanifol.musicserver.service.dto.TokenResponse;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    @Value("${musicserver.app.jwt.secret}")
    private String jwtSecret;
    @Value("${musicserver.app.jwt.access-exp}")
    private long accessTokenExp;
    @Value("${musicserver.app.jwt.refresh-exp}")
    private long refreshTokenExp;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            return false;
        }
    }

    public String getUsername(String token) {
        return Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String generateJwtToken(String username, List<String> roles, long exp, String tokenType) {
        return Jwts.builder()
                .header()
                .add("tokenType", tokenType)
                .and()
                .subject(username)
                .claim("roles", roles)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + exp))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(String username, List<String> roles) {
        return generateJwtToken(username, roles, refreshTokenExp, "refresh");
    }

    public String generateAccessToken(String username, List<String> roles) {
        return generateJwtToken(username, roles, accessTokenExp, "access");
    }

    public TokenResponse generateTokenResponse(Authentication auth) {
        String username = auth.getName();
        List<String> roles = auth
                .getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        String accessToken = generateAccessToken(username, roles);
        String refreshToken = generateRefreshToken(username, roles);
        return new TokenResponse(accessToken, refreshToken);
    }
}
