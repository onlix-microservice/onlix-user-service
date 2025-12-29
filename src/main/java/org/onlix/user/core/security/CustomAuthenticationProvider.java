//package org.onlyonce.user.core.security;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class CustomAuthenticationProvider implements AuthenticationProvider {
//
//    private final CustomUserDetailsService userDetailsService;
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        String loginId = authentication.getName(); // 사용자가 입력한 ID
//        String password = authentication.getCredentials().toString();
//
//        // DB에서 사용자 조회
//        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(loginId);
//
//        // 비밀번호 검증
//        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
//            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
//        }
//
//        // 인증 성공 -> Authentication 객체 반환
//        return new UsernamePasswordAuthenticationToken(
//                userDetails,  // principal
//                null,         // credentials
//                userDetails.getAuthorities() // 권한
//        );
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
//    }
//}