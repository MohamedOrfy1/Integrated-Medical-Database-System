package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;
import com.example.demo.model.Doctor;

public interface DoctorService {
    Doctor createDoctor(Doctor doctor);
    List<Doctor> getAllDoctors();
    Doctor getDoctorById(Long id);
    void deleteDoctor(Long id);
    boolean checkDoc(String user,String pass);
    String getAllDiagnosisJson();
    String getDoctorsInJson();
    String getPatientsDiagnosedby(String DiagnosisId);
    String getDocID(String username,String pass);
    boolean diagnosePatient(String diagnosisId, LocalDate diagnosisDate, String doctorId, String PatientId);
}