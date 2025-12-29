package org.onlix.user.auth.dto;

import lombok.Builder;

@Builder
public record JwtResponse(
    String accessToken,
    String refreshToken,
    String tokenType,
    long expiresIn,
    long refreshExpiresIn,
    LoginResponse loginInfo
) {
}
