package org.onlyonce.user.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authService.login(loginRequestDto.getUsername(), loginRequestDto.getPassword()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestParam String username, @RequestParam String refreshToken) {
        return ResponseEntity.ok(authService.refresh(username, refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam String username) {
        authService.logout(username);

        return ResponseEntity.ok("Logged out");
    }
}
