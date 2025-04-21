package com.example.demo.service;




public interface CommonService {
    String hashPassword(String Password);
    boolean verifyPassword(String password, String storedHash);
    String generateToken(String id, String role);
    boolean validateToken(String token);
    String extractID(String token);
    String extractRole(String token);

}