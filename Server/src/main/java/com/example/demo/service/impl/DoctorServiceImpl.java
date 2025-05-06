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


import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DiagnosisRepository diagnosisRepository;
    private final PatientDiagnosisRepository patientDiagnosisRepository;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository,DiagnosisRepository diagnosisRepository,PatientDiagnosisRepository patientDiagnosisRepository) {
        this.doctorRepository = doctorRepository;
        this.diagnosisRepository = diagnosisRepository;
        this.patientDiagnosisRepository = patientDiagnosisRepository;
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
}