package com.example.demo.controller;

import com.example.demo.service.DoctorService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import com.example.demo.model.Doctor;
import org.springframework.http.ResponseEntity;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger logger = LoggerFactory.getLogger(DoctorController.class);


    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping("/login")
    public boolean login(@RequestBody Doctor doc) {
        String username = doc.getUsername();
        String password = doc.getPassword();

        String sql = "SELECT COUNT(*) FROM Doctor WHERE username = ? AND password = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username, password);
        return count > 0;

    }

    @GetMapping("/getall")
    public ResponseEntity<String> getAllDoctors() {
        return ResponseEntity.ok("Check");
    }

    @GetMapping("/{id}")
    public Doctor getDoctorById(@PathVariable Long id) {
        return doctorService.getDoctorById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
    }
}