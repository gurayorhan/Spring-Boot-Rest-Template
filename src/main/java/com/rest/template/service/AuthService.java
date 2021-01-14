package com.rest.template.service;

import com.rest.template.dto.AuthRequest;
import com.rest.template.dto.AuthResponse;
import com.rest.template.security.token.TokenManagement;
import com.rest.template.security.token.TokenProvider;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private TokenManagement tokenManagement;

    @Autowired
    private TokenProvider tokenProvider;

    public AuthResponse login(AuthRequest authenticationRequest) throws Exception {
        try {
            try {
                String token = tokenManagement.createSession(authenticationRequest.getUsername(),authenticationRequest.getPassword());
                AuthResponse authenticationResponse = new AuthResponse(token);
                return authenticationResponse;
            }catch (Exception e){
                throw new Exception("Username or Password Wrong");
            }
        }catch (Exception e){
            throw new Exception("Login Error");
        }
    }

    public Boolean logout(AuthResponse authResponse) throws Exception {
        try{
            String token = tokenProvider.resolveToken(authResponse.getJwt());
            if(tokenManagement.deleteSession(token)){
                return true;
            }else{
                throw new Exception("Invalidate");
            }
        }catch (JwtException | IllegalArgumentException e){
            throw new Exception("Logout Invalidate");
        }
    }

}
