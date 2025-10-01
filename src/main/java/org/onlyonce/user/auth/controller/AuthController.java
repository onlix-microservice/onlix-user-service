package org.onlyonce.user.auth.controller;

import lombok.RequiredArgsConstructor;
import org.onlyonce.user.auth.service.AuthService;
import org.onlyonce.user.auth.dto.JwtRequestDto;
import org.onlyonce.user.auth.dto.JwtResponseDto;
import org.onlyonce.user.auth.dto.LoginRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody LoginRequestDto loginRequestDto) {
        authService.signin(loginRequestDto.getLoginId(), loginRequestDto.getPassword());

        return ResponseEntity.ok("회원가입 완료되었습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authService.login(loginRequestDto.getLoginId(), loginRequestDto.getPassword()));
    }

    // AccessToken 재발행
    @PostMapping("/refresh")
    public ResponseEntity<JwtResponseDto> refresh(@RequestBody JwtRequestDto jwtRequestDto) {
        String loginId = jwtRequestDto.getLoginId();
        String refreshToken = jwtRequestDto.getRefreshToken();

        return ResponseEntity.ok(authService.refresh(loginId, refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam String loginId) {
        authService.logout(loginId);

        return ResponseEntity.ok("Logged out");
    }

    @PostMapping("/admin")
    public ResponseEntity<?> admin() {

        return ResponseEntity.ok("Logged out");
    }
}
