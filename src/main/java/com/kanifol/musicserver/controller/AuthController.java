package com.kanifol.musicserver.controller;

import com.kanifol.musicserver.service.JwtService;
import com.kanifol.musicserver.service.dto.RegisterRequest;
import com.kanifol.musicserver.service.dto.TokenResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "auth/")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

//    @PostMapping("/register")
//    public TokenResponse register(@RequestBody RegisterRequest request) {
//        Authentication auth =
//    }
}
