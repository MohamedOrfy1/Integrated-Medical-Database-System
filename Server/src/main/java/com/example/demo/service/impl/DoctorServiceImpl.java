package com.example.demo.service.impl;

import com.example.demo.repository.*;
import com.example.demo.service.DoctorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import  com.example.demo.model.*;
import com.example.demo.service.DoctorService;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DiagnosisRepository diagnosisRepository;
    private final PatientDiagnosisRepository patientDiagnosisRepository;
    private final AdmissionRepository admissionRepository;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository,DiagnosisRepository diagnosisRepository,PatientDiagnosisRepository patientDiagnosisRepository,
                             AdmissionRepository admissionRepository) {
        this.doctorRepository = doctorRepository;
        this.diagnosisRepository = diagnosisRepository;
        this.patientDiagnosisRepository = patientDiagnosisRepository;
        this.admissionRepository = admissionRepository;
    }

    @Override
    public Doctor createDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public String getDocID(String username,String pass){
        Optional<Doctor> d = doctorRepository.findByUsernameAndPassword(username,pass);
        return d.map(Doctor::getDoctorId).orElse(null);
    }

    @Override
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

    @Override
    public boolean checkDoc(String user,String pass){
        return false;
    }

    @Override
    public String getDoctorsInJson() {
        List<Doctor> doctors = doctorRepository.findAll();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(doctors);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public String getAllDiagnosisJson(){
        List <Diagnosis> diagnosis = diagnosisRepository.findAllByOrderByDiagnosisNameAsc();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(diagnosis);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public boolean diagnosePatient(String diagnosisId, LocalDate diagnosisDate,String doctorId,String PatientId){
        return patientDiagnosisRepository.insertPatientDiagnosis(PatientId, diagnosisId, diagnosisDate, doctorId) > 0;
    }

    @Override
    public String getPatientsDiagnosedby(String DiagnosisId){
        List <Patient> pat = patientDiagnosisRepository.findPatientsWithDiagnosis(DiagnosisId);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(pat);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public String getPatientsDoc(String DocID){
        List <Patient> pats = admissionRepository.findPatientsByDoctorId(DocID);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(pats);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public boolean addDiagnosis(String diagnosisCode, String diagnosisName) {
        // Check if diagnosis code already exists
        if (diagnosisRepository.existsByDiagnosisCode(diagnosisCode)) {
            return false;
        }
        // Create and save new diagnosis
        Diagnosis newDiagnosis = new Diagnosis();
        newDiagnosis.setDiagnosisCode(diagnosisCode);
        newDiagnosis.setDiagnosisName(diagnosisName);
        diagnosisRepository.save(newDiagnosis);
        return true;
    }

    @Override
    public boolean deleteDiagnosis(String diagnosisCode) {
        try {
            // Check if diagnosis exists first
            if (!diagnosisRepository.existsById(diagnosisCode)) {
                return false;
            }

            // Check if diagnosis is being used in patient_diagnosis table
            if (diagnosisRepository.isDiagnosisInUse(diagnosisCode) > 0) {
                return false;
            }

            diagnosisRepository.deleteById(diagnosisCode);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}