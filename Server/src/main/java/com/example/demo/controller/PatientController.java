package com.example.demo.controller;

import com.example.demo.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.Patient;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    public Patient createPatient(@RequestBody Patient patient) {
        return null;
    }

    @GetMapping
    public List<Patient> getAllPatients() {
        return null;
    }

    @GetMapping("/{id}")
    public Patient getPatientById(@PathVariable Long id) {
        return null;
    }

    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable Long id) {

    }
}