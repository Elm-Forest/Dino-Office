package com.ctgu.oaweb.security.config;

import org.apache.shiro.authc.UsernamePasswordToken;

public class JwtToken extends UsernamePasswordToken {
    private final String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
