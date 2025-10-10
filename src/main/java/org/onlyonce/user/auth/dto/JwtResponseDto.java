package org.onlyonce.user.auth.dto;

import lombok.Builder;

@Builder
public record JwtResponseDto(
    String accessToken,
    String refreshToken,
    String tokenType,
    long expiresIn,
    long refreshExpiresIn,
    LoginResponseDto loginInfo
) {
}
