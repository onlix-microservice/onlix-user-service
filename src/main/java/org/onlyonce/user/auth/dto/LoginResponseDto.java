package org.onlyonce.user.auth.dto;

import lombok.Builder;
import org.onlyonce.user.core.security.CustomUserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
public record LoginResponseDto(
    String loginId,
    Set<String> roles,
    LocalDateTime lastLoginAt
){
    public static LoginResponseDto fromUserDetails(CustomUserDetails userDetails){
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return LoginResponseDto.builder()
                .loginId(userDetails.getUsername())
                .roles(roles)
                .lastLoginAt(LocalDateTime.now())
                .build();
    }
}
