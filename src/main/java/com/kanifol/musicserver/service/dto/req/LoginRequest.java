package com.kanifol.musicserver.service.dto.req;

public record LoginRequest(
        String username,
        String password
) {
}
