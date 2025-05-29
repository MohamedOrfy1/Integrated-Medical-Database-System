package com.example.demo.service.impl;

import com.example.demo.repository.*;
import com.example.demo.service.DoctorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.persistence.criteria.CriteriaBuilder;
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
    private final PatientTestRepository patientTestRepository;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository,DiagnosisRepository diagnosisRepository,PatientDiagnosisRepository patientDiagnosisRepository,
                             AdmissionRepository admissionRepository,PatientTestRepository patientTestRepository) {
        this.doctorRepository = doctorRepository;
        this.diagnosisRepository = diagnosisRepository;
        this.patientDiagnosisRepository = patientDiagnosisRepository;
        this.admissionRepository = admissionRepository;
        this.patientTestRepository = patientTestRepository;
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
    public String getPatTestsIds(String PatID){
        List<Integer> testIDs = patientTestRepository.findTestIdsByPatientId(PatID);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(testIDs);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public String getPatientDataJson(String patientId){

        Patient p = null;

        JsonObject PatientInfo = new JsonObject();
        PatientInfo.addProperty("first_name", "XX");
        PatientInfo.addProperty("family_name", "XX");
        PatientInfo.addProperty("registrationDate", "XX");
        PatientInfo.addProperty("age", "XX");
        PatientInfo.addProperty("PatientTestID", "");


        List<Integer> testIDs = patientTestRepository.findTestIdsByPatientId(patientId);
        JsonArray TestIDs = new JsonArray();
        int n = testIDs.size();
        for (Integer testID : testIDs) {
            TestIDs.add(testID);
        }
        PatientInfo.add("TestIDs", TestIDs);


        JsonArray Diagnosis = new JsonArray();
        for (int i = 0 ; i<n;i++){
            JsonObject diagnose = new JsonObject();
            diagnose.addProperty("DiagnosisDate", "XX");
            diagnose.addProperty("Diagnosis", "XX");
            Diagnosis.add(diagnose);
        }
        PatientInfo.add("Diagnosis", Diagnosis);


        return null;
    }
}