package com.example.demo.service;

import com.example.demo.model.Patient;

import java.time.LocalDate;
import java.util.Optional;

public interface PatientSearchService {
    public Optional<Patient> searchAdmittedPatients(String doctorId, String PatientId);
    public LocalDate getPatientDOB(String yymmdd, int centuryDigit);
    public int calculateAge(Patient patient);
    public String getPatientGender(int digit);

}