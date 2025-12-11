package org.onlix.user.auth.dto;

import lombok.Getter;

@Getter
public class JwtRequest {
    private String loginId;
    private String refreshToken;
}
