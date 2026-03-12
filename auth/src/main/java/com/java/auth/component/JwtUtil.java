package com.java.auth.component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private final String SECRET = "mySuperSecretKeyForJWTSigningThatIsLongEnough";
    private final String REFRESHSECRET = "mySupdsdfsdfsdfserSecretKeyForJWTSigningThatIsLongEnough";

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public String generateTokenRefresh(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 5))
                .signWith(SignatureAlgorithm.HS256, REFRESHSECRET)
                .compact();
    }

    public boolean validateToken(String token, String username) {
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }

    public boolean validateTokenRefresh(String token, String username) {
        return extractUsernameRefresh(token).equals(username) && !isTokenExpiredRefresh(token);
    }

    public String extractUsername(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody().getSubject();
    }

    public String extractUsernameRefresh(String token) {
        return Jwts.parser().setSigningKey(REFRESHSECRET).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean isTokenExpired(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody().getExpiration().before(new Date());
    }

    public boolean isTokenExpiredRefresh(String token) {
        return Jwts.parser().setSigningKey(REFRESHSECRET).parseClaimsJws(token).getBody().getExpiration().before(new Date());
    }
}
