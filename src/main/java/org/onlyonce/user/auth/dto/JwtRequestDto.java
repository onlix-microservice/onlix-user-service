package org.onlyonce.user.auth.dto;

import lombok.Getter;

@Getter
public class JwtRequestDto {
    private String loginId;
    private String refreshToken;
}
