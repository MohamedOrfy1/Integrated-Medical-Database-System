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


    private int extractAgeFromDOB(LocalDate DOB){
        if (DOB == null) {
            throw new IllegalArgumentException("Birth date cannot be null");
        }
        LocalDate currentDate = LocalDate.now();
        return Period.between(DOB, currentDate).getYears();

    }


    private LocalDate extractDOBFromID(String id){
        if (id == null || id.length() != 14 || !id.matches("\\d{14}")) {
            throw new IllegalArgumentException("ID must be exactly 14 numeric characters");
        }

        String centuryDigit = id.substring(0, 1);  // x (determines century)
        String yy = id.substring(1, 3);            // yy (last two digits of year)
        String mm = id.substring(3, 5);            // mm (month)
        String dd = id.substring(5, 7);            // dd (day)

        // Determine full year based on century digit
        int century;
        switch (centuryDigit) {
            case "2": century = 1900; break;  // 1900-1999
            case "3": century = 2000; break;  // 2000-2099
            default:
                throw new IllegalArgumentException("Invalid century digit in ID");
        }

        int year = century + Integer.parseInt(yy);
        int month = Integer.parseInt(mm);
        int day = Integer.parseInt(dd);

        try {
            return LocalDate.of(year, month, day);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date in ID: " + e.getMessage());
        }

    }


    @Override
    public int getAgeFromID(String ID){
        return extractAgeFromDOB(extractDOBFromID(ID));
    }


    }






