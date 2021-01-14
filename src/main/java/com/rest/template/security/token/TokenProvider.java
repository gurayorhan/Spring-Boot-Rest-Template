package com.rest.template.security.token;

import com.rest.template.model.User;
import com.rest.template.service.UserService;
import io.jsonwebtoken.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Function;

@Component
public class TokenProvider {

    private String SECRET_KEY = "secret";// Key

    @Autowired
    private UserService userService;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public User getToUser(String token) throws Exception {
        return userService.findById(extractUsername(token));
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String resolveToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String generateToken(String username, String password) throws Exception {
        User user = userService.findById(username);
        if(!DigestUtils.sha256Hex(password).equals(user.getPassword())){
            throw new Exception("Password Error");
        }
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, user.getUsername());
    }

    public String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder().setClaims(claims).setSubject(username)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

}
