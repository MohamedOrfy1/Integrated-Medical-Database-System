package com.example.demo.service;


public interface JWTService {
    String getIDFromToken(String token);
    String getRoleFromToken(String token);
    boolean isTokenExpired(String token);
    boolean isTokenValid(String token);
    String generateToken(String id, String role);

}