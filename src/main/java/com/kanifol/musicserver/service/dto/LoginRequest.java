package com.kanifol.musicserver.service.dto;

public record LoginRequest(
        String username,
        String password
) {
}
