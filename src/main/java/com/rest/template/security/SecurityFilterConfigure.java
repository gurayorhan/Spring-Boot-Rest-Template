package com.rest.template.security;

import com.rest.template.security.token.TokenManagement;
import com.rest.template.security.token.TokenProvider;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class SecurityFilterConfigure extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private TokenProvider tokenProvider;

    private TokenManagement tokenManagement;

    public SecurityFilterConfigure(TokenProvider tokenProvider, TokenManagement tokenManagement) {
        this.tokenProvider = tokenProvider;
        this.tokenManagement = tokenManagement;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        SecurityFilter customFilter = new SecurityFilter(tokenProvider,tokenManagement);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
