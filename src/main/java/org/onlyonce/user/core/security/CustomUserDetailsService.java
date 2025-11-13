package org.onlyonce.user.core.security;

import lombok.RequiredArgsConstructor;
import org.onlyonce.user.account.domain.entity.AccountEntity;
import org.onlyonce.user.account.domain.repository.AccountRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        AccountEntity account = accountRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다."));

        Collection<? extends GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_" + account.getRole().name()));

        return new CustomUserDetails(
                account.getLoginId(),
                account.getPassword(),
                authorities,
                account.getStatus()
        );
    }
}