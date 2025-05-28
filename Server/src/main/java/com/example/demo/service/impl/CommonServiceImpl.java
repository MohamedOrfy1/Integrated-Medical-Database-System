package com.example.demo.service.impl;


import com.example.demo.service.CommonService;
import org.springframework.stereotype.Service;
import java.security.MessageDigest;
import io.jsonwebtoken.*;

import java.time.LocalDate;
import java.time.Period;
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
    public int extractAgeFromDOB(LocalDate DOB){
        if (DOB == null) {
            throw new IllegalArgumentException("Birth date cannot be null");
        }
        LocalDate currentDate = LocalDate.now();
        return Period.between(DOB, currentDate).getYears();

    }


    }






