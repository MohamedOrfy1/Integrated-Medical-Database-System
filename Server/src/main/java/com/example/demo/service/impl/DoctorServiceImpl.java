package com.example.demo.service.impl;

import com.example.demo.DTO.BloodTestAttributeDTO;
import com.example.demo.DTO.DiagnosisDTO;
import com.example.demo.DTO.DiagnosisPatientDTO;
import com.example.demo.DTO.TestHeaderInfoDTO;
import com.example.demo.repository.*;
import com.example.demo.service.DoctorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import  com.example.demo.model.*;
import com.example.demo.service.DoctorService;


import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DiagnosisRepository diagnosisRepository;
    private final PatientDiagnosisRepository patientDiagnosisRepository;
    private final AdmissionRepository admissionRepository;
    private final PatientTestRepository patientTestRepository;
    private final EmployeeServiceImpl employeeService;
    private final CommonServiceImpl commonService;
    private final TestAttributeRepository testAttributeRepository;
    private final PDFGenServiceImpl pdfGenService;
    private final BloodTestRepository bloodTestRepository;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository,DiagnosisRepository diagnosisRepository,PatientDiagnosisRepository patientDiagnosisRepository,
                             AdmissionRepository admissionRepository,PatientTestRepository patientTestRepository,EmployeeServiceImpl employeeService,CommonServiceImpl commonService, TestAttributeRepository testAttributeRepository,PDFGenServiceImpl pdfGenService,
                             BloodTestRepository bloodTestRepository) {
        this.doctorRepository = doctorRepository;
        this.diagnosisRepository = diagnosisRepository;
        this.patientDiagnosisRepository = patientDiagnosisRepository;
        this.admissionRepository = admissionRepository;
        this.patientTestRepository = patientTestRepository;
        this.employeeService = employeeService;
        this.commonService = commonService;
        this.testAttributeRepository = testAttributeRepository;
        this.pdfGenService = pdfGenService;
        this.bloodTestRepository = bloodTestRepository;
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
        System.out.println(patientId);

        Patient p = employeeService.getPatientByID(patientId);

        JsonObject PatientInfo = new JsonObject();
        PatientInfo.addProperty("first_name", p.getFirstName());
        PatientInfo.addProperty("family_name", p.getFamilyName());
        PatientInfo.addProperty("registrationDate", p.getRegistrationDate().toString());
        PatientInfo.addProperty("age", commonService.getAgeFromID(patientId));


        List<Integer> testIDs = patientTestRepository.findTestIdsByPatientId(patientId);
        JsonArray TestIDs = new JsonArray();
        int n = testIDs.size();
        for (Integer testID : testIDs) {
            TestIDs.add(testID);
        }
        PatientInfo.add("TestIDs", TestIDs);


        JsonArray Diagnosis = new JsonArray();
        List <DiagnosisDTO> dtos = patientDiagnosisRepository.findDiagnosisDetailsByPatientId(patientId);
        for (DiagnosisDTO dto:dtos){
            JsonObject diagnose = new JsonObject();
            diagnose.addProperty("DiagnosisDate", dto.getDiagnosisDate().toString());
            diagnose.addProperty("Diagnosis", dto.getDiagnosisName());
            diagnose.addProperty("DiagnosisDoctor", dto.getDoctorName());
            Diagnosis.add(diagnose);
        }
        PatientInfo.add("Diagnosis", Diagnosis);

        Gson gson = new Gson();
        return gson.toJson(PatientInfo);
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

    @Override
    public String getDiagnosedPatients(String sortBy){
        String parameter;
        //System.out.println(sortBy);
        if (Objects.equals(sortBy, "1")){
            System.out.println("d.diagnosisName");
            parameter = "d.diagnosisName";
        }else if(Objects.equals(sortBy, "2")){
            System.out.println("pd.id.patientId");
            parameter = "pd.id.patientId";
        }else if(Objects.equals(sortBy, "3")){
            System.out.println("pd.id.diagnosisDate");
            parameter = "pd.id.diagnosisDate";
        }else{
            System.out.println("d.diagnosisName--else");
            parameter = "d.diagnosisName";
        }
        List<DiagnosisPatientDTO> pts = patientDiagnosisRepository.getAllDiagnosisPatients(parameter);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(pts);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public void genReport(Integer TestId){
        List<BloodTestAttributeDTO> atts =testAttributeRepository.findTestAttributesWithRangesAsDTO(TestId);
        TestHeaderInfoDTO hd = bloodTestRepository.findBloodTestReportByTestId(TestId);
        try {

            pdfGenService.convertXhtmlToPdf(pdfGenService.replacePlaceholdersFromDTO(hd,atts));
        }catch(Exception e){
            System.out.println(e);
        }


    }
}