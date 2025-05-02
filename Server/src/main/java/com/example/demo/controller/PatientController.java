package com.example.demo.controller;

import com.example.demo.service.CommonService;
import com.example.demo.service.DoctorService;
import com.example.demo.service.PatientService;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.Patient;
import org.springframework.http.ResponseEntity;

import java.util.List;
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public PatientController(PatientService patientservice) {
        this.patientService = patientservice;
    }

    @GetMapping("/getPatients")
    public String getAllPatients() {
        return patientService.getPatientsInJson();
    }
    @PostMapping("/add")
    @Transactional
    public ResponseEntity<Boolean> addPatient(@RequestBody String JsonPatient) {
        if (patientService.InsertJsonPatient(JsonPatient)) return ResponseEntity.ok(true);
        else return ResponseEntity.ok(false);

    }



    @GetMapping("/{id}")
    public Patient getPatientById(@PathVariable Long id) {
        return null;
    }

    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable Long id) {

    }
}