package com.example.demo.controller;

import com.example.demo.service.CommonService;
import com.example.demo.service.DoctorService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import com.example.demo.model.Doctor;
import org.springframework.http.ResponseEntity;
import org.slf4j.LoggerFactory;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;
    private final CommonService commonService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger logger = LoggerFactory.getLogger(DoctorController.class);


    @Autowired
    public DoctorController(DoctorService doctorService, CommonService commonService) {
        this.doctorService = doctorService;
        this.commonService = commonService;

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
//    public ResponseEntity<String> getAllDoctors() {
//        return ResponseEntity.ok(doctorService.getAllDoctors());
//    }
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/{id}")
    public Doctor getDoctorById(@PathVariable String id) {
        return doctorService.getDoctorById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable String id) {
        doctorService.deleteDoctor(id);
    }
}