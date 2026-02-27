package com.kanifol.musicserver.controller;

import com.kanifol.musicserver.repository.model.User;
import com.kanifol.musicserver.service.JwtService;
import com.kanifol.musicserver.service.UserService;
import com.kanifol.musicserver.service.dto.LoginRequest;
import com.kanifol.musicserver.service.dto.RefreshRequest;
import com.kanifol.musicserver.service.dto.RegisterRequest;
import com.kanifol.musicserver.service.dto.TokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "auth/")
public class AuthController {

    private final JwtService jwtService;
    private final UserService userService;

    public AuthController(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshToken(@RequestBody RefreshRequest request) {
        if (!jwtService.validateToken(request.refreshToken()))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        User user = userService.getUserByUsername(request.username());
        return ResponseEntity.ok(
                jwtService.generateAccessToken(user.getUsername(), user.getRolesNames())
        );
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody RegisterRequest request) {
        Authentication auth = userService.registerUser(request);
        return ResponseEntity.ok(jwtService.generateTokenResponse(auth));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        try {
            Authentication auth = userService.login(request);
            return new ResponseEntity<>(jwtService.generateTokenResponse(auth), HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
