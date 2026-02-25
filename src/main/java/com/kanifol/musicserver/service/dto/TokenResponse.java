package com.kanifol.musicserver.service.dto;

public record TokenResponse(
   String accessToken,
   String refreshToken
) {}
