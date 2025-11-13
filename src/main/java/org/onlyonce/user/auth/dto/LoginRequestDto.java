package org.onlyonce.user.auth.dto;

public record LoginRequestDto(
        String loginId,
        String password,
        String deviceId
) {
}