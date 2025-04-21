package com.example.demo.service.impl;


import com.example.demo.service.CommonService;
import org.springframework.stereotype.Service;
import java.security.MessageDigest;
import io.jsonwebtoken.*;
import java.util.Date;
import org.springframework.stereotype.Service;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


@Service
public class CommonServiceImpl implements CommonService {
    public CommonServiceImpl() {
    }

    private final String SECRET = System.getenv("JWT_SECRET");

    @Override
    public String hashPassword(String password){
        try {
            String combined = password + System.getProperty("PEPPER");
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(combined.getBytes());
            return Base64.getEncoder().encodeToString(hashBytes);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    @Override
    public boolean verifyPassword(String password, String storedHash) {
        String newHash = hashPassword(password);
        return newHash.equals(storedHash);
    }

    @Override
    public String generateToken(String id, String role) {
        return Jwts.builder()
                .setSubject(id)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1800000)) //half an hour
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    @Override
    public String extractID(String token) {
        return Jwts.parser().setSigningKey(SECRET)
                .parseClaimsJws(token).getBody().getSubject();
    }

    @Override
    public String extractRole(String token) {
        return (String) Jwts.parser().setSigningKey(SECRET)
                .parseClaimsJws(token).getBody().get("role");
    }


    }






