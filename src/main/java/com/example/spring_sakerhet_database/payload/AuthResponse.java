package com.example.spring_sakerhet_database.payload;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private Collection<? extends GrantedAuthority> roles;

    public AuthResponse(String accessToken, String refreshToken, Collection<? extends GrantedAuthority> roles) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.roles = roles;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Collection<? extends GrantedAuthority> getRoles() {
        return roles;
    }
}
