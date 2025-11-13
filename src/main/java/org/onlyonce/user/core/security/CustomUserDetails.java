package org.onlyonce.user.core.security;

import org.onlyonce.user.account.enums.Status;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final String loginId;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Status status;

    public CustomUserDetails(String loginId, String password, Collection<? extends GrantedAuthority> authorities, Status status) {
        this.loginId = loginId;
        this.password = password;
        this.authorities = authorities;
        this.status = status;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return loginId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.status == Status.ACTIVE;
    }
}