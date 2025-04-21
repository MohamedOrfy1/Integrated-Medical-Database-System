package com.example.demo.service;

import com.example.demo.model.Patient;

import java.util.List;

public interface PatientService {
    Boolean InsertJsonPatient(String JsonPatient);
    String getPatientsInJson();
    Boolean isPatientExist(String id);
    Boolean isPatientValid(Patient patient);

}