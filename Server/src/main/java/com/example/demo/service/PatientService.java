package com.example.demo.service;

import com.example.demo.model.Patient;

import java.util.List;

public interface PatientService {
    Patient createPatient(Patient patient);
    String getPatientsInJson();

}