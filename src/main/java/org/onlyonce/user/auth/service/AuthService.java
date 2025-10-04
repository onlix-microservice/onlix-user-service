package org.onlyonce.user.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import org.onlyonce.user.auth.dto.JwtResponseDto;
import org.onlyonce.user.auth.dto.LoginRequestDto;

public interface AuthService {
    void signin(LoginRequestDto loginRequestDto);
    JwtResponseDto login(LoginRequestDto loginRequestDto);
    JwtResponseDto refresh(HttpServletRequest request);
    void logout(HttpServletRequest request);

}
