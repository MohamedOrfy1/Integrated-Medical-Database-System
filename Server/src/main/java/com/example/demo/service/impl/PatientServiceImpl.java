package com.example.demo.service.impl;

import com.example.demo.repository.PatientRepository;
import com.example.demo.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.Patient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient createPatient(Patient patient) {
        return null;
    }

    @Override
    public String getPatientsInJson() {
        List<Patient> patients = patientRepository.findAll();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(patients);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert patients to JSON", e);
        }
    }


}