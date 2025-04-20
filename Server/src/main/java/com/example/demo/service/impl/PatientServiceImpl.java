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
    public Boolean InsertJsonPatient (String JsonPatient) {

        Patient p = new Patient();
        int affected_rows = 0;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            p = objectMapper.readValue(JsonPatient, Patient.class);
        }catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid patient JSON: " + e.getOriginalMessage());
        }
        if (!isPatientExist(p.getPatientId()))
        { affected_rows = patientRepository.insertPatient(p);
        }


        return affected_rows > 0;
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

    @Override
    public Boolean isPatientExist(String id) {
        if(patientRepository.findByPatientId(id).isEmpty()) return false;
        else return true;
    }



}