package org.onlix.user.auth.dto;

import lombok.Builder;
import org.onlix.user.core.security.CustomUserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
public record LoginResponse(
    String loginId,
    Set<String> roles,
    LocalDateTime lastLoginAt
){
    public static LoginResponse fromUserDetails(CustomUserDetails userDetails){
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return LoginResponse.builder()
                .loginId(userDetails.getUsername())
                .roles(roles)
                .lastLoginAt(LocalDateTime.now())
                .build();
    }
}
