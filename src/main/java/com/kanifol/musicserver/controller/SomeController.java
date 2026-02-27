package com.kanifol.musicserver.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SomeController {

    @GetMapping("/me")
    public String me(Authentication authentication) {
        return authentication.getName();
    }
}
