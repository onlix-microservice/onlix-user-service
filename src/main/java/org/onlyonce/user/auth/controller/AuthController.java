package org.onlyonce.user.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.onlyonce.user.auth.dto.JwtResponseDto;
import org.onlyonce.user.auth.dto.LoginRequestDto;
import org.onlyonce.user.auth.service.AuthService;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody LoginRequestDto loginRequestDto) {
        authService.signin(loginRequestDto);

        return ResponseEntity.ok("회원가입 완료되었습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {
        JwtResponseDto jwtResponseDto = authService.login(loginRequestDto);

        ResponseCookie accessCookie = ResponseCookie.from("accessToken", jwtResponseDto.accessToken())
                .httpOnly(true)     // JS 접근 불가
                .secure(true)       // HTTPS에서만 전송
                .sameSite("Strict") // CSRF 방어
                .path("/")          // 전역 적용
                .maxAge(jwtResponseDto.expiresIn())
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", jwtResponseDto.refreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(jwtResponseDto.refreshExpiresIn())
                .build();

        return ResponseEntity.noContent()
                .header("Set-Cookie", accessCookie.toString())
                .header("Set-Cookie", refreshCookie.toString())
                .build();
    }

    // AccessToken 재발행
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request) {
        JwtResponseDto jwtResponseDto = authService.refresh(request);

        ResponseCookie accessCookie = ResponseCookie.from("accessToken", jwtResponseDto.accessToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(jwtResponseDto.expiresIn())
                .build();

        return ResponseEntity.noContent()
                .header("Set-Cookie", accessCookie.toString())
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        authService.logout(request);

        ResponseCookie deleteAccess = ResponseCookie.from("accessToken", "")
                .path("/")
                .httpOnly(true)
                .secure(true)
                .maxAge(0)
                .build();

        ResponseCookie deleteRefresh = ResponseCookie.from("refreshToken", "")
                .path("/")
                .httpOnly(true)
                .secure(true)
                .maxAge(0)
                .build();

        return ResponseEntity.noContent()
                .header("Set-Cookie", deleteAccess.toString())
                .header("Set-Cookie", deleteRefresh.toString())
                .build();
    }

    @PostMapping("/admin")
    public ResponseEntity<?> admin() {

        return ResponseEntity.ok("Logged out");
    }
}
