package com.kanifol.musicserver.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SomeController {

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/me")
    public String me(Authentication authentication) {
        return authentication.getName();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin_route")
    public String admin_route() { return "hello from admin route"; }
}
