package org.onlix.user.auth.dto;

public record LoginRequest(
        String loginId,
        String password,
        String deviceId
) {
}