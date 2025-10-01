package org.onlyonce.user.auth.service;

import org.onlyonce.user.auth.dto.JwtResponseDto;

public interface AuthService {
    void signin(String loginId, String password);
    void logout(String loginId);
    JwtResponseDto login(String loginId, String password);
    JwtResponseDto refresh(String loginId, String refreshToken);

}
