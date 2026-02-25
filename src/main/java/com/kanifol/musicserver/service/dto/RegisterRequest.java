package com.kanifol.musicserver.service.dto;

public record RegisterRequest(
        String username,
        String email,
        String password
) {}
