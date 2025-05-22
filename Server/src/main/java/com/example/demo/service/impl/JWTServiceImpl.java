package com.example.demo.service.impl;


import com.example.demo.service.JWTService;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.*;

import java.util.Date;
import io.github.cdimascio.dotenv.Dotenv;

@Service
public class JWTServiceImpl implements JWTService {
    Dotenv dotenv = Dotenv.load();
    private final String secret = dotenv.get("jwt.secret");
    private final long expirationDuration = Long.parseLong(dotenv.get("jwt.expiration"));

    @Override
    public String generateToken(String id, String role) {
        return Jwts.builder()
                .setSubject(id)
                .claim("role", role)  // Single role claim
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationDuration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    @Override
    public String getIDFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    @Override
    public String getRoleFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    @Override
    public boolean isTokenExpired(String token) {
        try {
            final Date expiration = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();
            return expiration.before(new Date());
        } catch (ExpiredJwtException ex) {
            return true; // Token is expired
        } catch (Exception e) {
            return true; // Other parsing errors treated as expired
        }
    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return  !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

}