package com.example.demo.service;




public interface CommonService {
    String hashPassword(String Password);
    boolean verifyPassword(String password, String storedHash);

}