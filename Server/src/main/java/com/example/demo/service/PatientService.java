package com.example.demo.service;

import com.example.demo.model.Patient;

import java.time.LocalDate;
import java.util.List;

public interface PatientService {
    Boolean InsertJsonPatient(String JsonPatient);
    String getPatientsInJson();
    Boolean isPatientExist(String id);
    Boolean isPatientValid(Patient patient);
    Boolean isValidBirthDate(String yymmdd, int centuryDigit);
    Boolean isValidNationalId(String nationalId);
    Boolean isValidPhoneNumber(String phoneNumber);



}