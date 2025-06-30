package com.example.demo.service;


import java.time.LocalDate;

public interface CommonService {
    String hashPassword(String Password);
    boolean verifyPassword(String password, String storedHash);
    int getAgeFromID(String ID);
    boolean isMale(String ID);


}