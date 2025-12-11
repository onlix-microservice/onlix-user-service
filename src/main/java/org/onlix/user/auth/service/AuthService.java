package org.onlix.user.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import org.onlix.user.auth.dto.JwtResponse;
import org.onlix.user.auth.dto.LoginRequest;

public interface AuthService {
    void signin(LoginRequest loginRequestDto);
    JwtResponse login(LoginRequest loginRequestDto);
    JwtResponse refresh(HttpServletRequest request);
    void logout(HttpServletRequest request);

}
