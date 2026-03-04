package com.kanifol.musicserver.service.dto.res;

public record TokenResponse(
   String accessToken,
   String refreshToken
) {}
