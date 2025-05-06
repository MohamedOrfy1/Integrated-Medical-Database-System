package com.example.demo.controller;

import com.example.demo.service.CommonService;
import com.example.demo.service.DoctorService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import com.example.demo.model.Doctor;
import org.springframework.http.ResponseEntity;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

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

    @GetMapping("/getdoc")
    public String getDoctors() { return doctorService.getDoctorsInJson(); }


    @PostMapping("/login")
    public boolean login(@RequestBody Doctor doc) {
        String username = doc.getUsername();
        String password = doc.getPassword();

        String sql = "SELECT COUNT(*) FROM Doctor WHERE username = ? AND password = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username, password);
        return count > 0;

    }

    @GetMapping("/getDiagnosis")
    public ResponseEntity<String> getAllDiagnosis() {
        String dig = doctorService.getAllDiagnosisJson();
        return ResponseEntity.ok(dig);
    }

    @PostMapping("/getPatDiagnosis")
    public ResponseEntity<String> getPatientsByDiagnosis(@RequestBody String DiagnosisId) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(DiagnosisId);
            String diagnosisId = rootNode.path("DiagnosisId").asText();
            return ResponseEntity.ok(doctorService.getPatientsDiagnosedby(diagnosisId));
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
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