package org.onlyonce.user.auth;

import java.util.Map;

public interface AuthService {
    Map<String, String> login(String username, String password);
    Map<String, String> refresh(String username, String refreshToken);
    void logout(String username);
}
