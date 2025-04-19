package com.example.demo.controller;

import com.example.demo.service.PatientService;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.Patient;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    //private final PatientService patientService;
    @PersistenceContext
    private EntityManager entityManager;



    @PostMapping("/add")
    @Transactional
    public ResponseEntity<Boolean> createPatient(@RequestBody Patient patient) {
        try {


            // Raw SQL query with parameters
            String sql = """
                INSERT INTO patient (
                    patient_id, first_name, father_name, grandfather_name,
                    family_name, phone_number, registration_date, email, name
                ) VALUES (
                    :patientId, :firstName, :fatherName, :grandfatherName,
                    :familyName, :phoneNumber, :registrationDate, :email, :name
                )
                """;

            // Execute query and get affected rows
            int rowsAffected = entityManager.createNativeQuery(sql)
                    .setParameter("patientId", patient.getPatientId())
                    .setParameter("firstName", patient.getFirstName())
                    .setParameter("fatherName", patient.getFatherName())
                    .setParameter("grandfatherName", patient.getGrandfatherName())
                    .setParameter("familyName", patient.getFamilyName())
                    .setParameter("phoneNumber", patient.getPhoneNumber())
                    .setParameter("registrationDate", patient.getRegistrationDate())
                    .setParameter("email", patient.getEmail())
                    .setParameter("name", patient.getName())
                    .executeUpdate();

            // Return true if inserted successfully
            return ResponseEntity.ok(rowsAffected > 0);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
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