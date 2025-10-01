package org.onlyonce.user.auth.dto;

import lombok.Getter;

@Getter
public class LoginRequestDto {
    private String loginId;
    private String password;
}
