package com.rest.template.controller;

import com.rest.template.dto.AuthRequest;
import com.rest.template.dto.AuthResponse;
import com.rest.template.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest AuthRequest) throws Exception {
        return ResponseEntity.ok(authService.login(AuthRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<Boolean> logout(@RequestBody AuthResponse AuthResponse) throws Exception {
        return ResponseEntity.ok(authService.logout(AuthResponse));
    }

}
