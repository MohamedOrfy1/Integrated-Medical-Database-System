package com.example.demo.controller;

import com.example.demo.service.CommonService;
import com.example.demo.service.DoctorService;
import com.example.demo.service.impl.JWTServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import com.example.demo.model.Doctor;
import com.example.demo.service.PDFGenService;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;
    private final CommonService commonService;
    private final PDFGenService pdfGenService;
    private final JWTServiceImpl jwtService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger logger = LoggerFactory.getLogger(DoctorController.class);


    @Autowired
    public DoctorController(DoctorService doctorService, CommonService commonService,PDFGenService pdfGenService,JWTServiceImpl jwtService) {
        this.doctorService = doctorService;
        this.commonService = commonService;
        this.pdfGenService = pdfGenService;
        this.jwtService = jwtService;

    }

    @PreAuthorize("hasAuthority('EMP')")
    @GetMapping("/getdoc")
    public String getDoctors() { return doctorService.getDoctorsInJson(); }


    @PostMapping("/login")
    public String login(@RequestBody Doctor doc) {
        String username = doc.getUsername();
        String password = doc.getPassword();

        String sql = "SELECT COUNT(*) FROM Doctor WHERE username = ? AND password = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username, password);
        if (count > 0){
            String id = doctorService.getDocID(username,password);
            String role = "DOC";
            return jwtService.generateToken(id,role);

        }else{
            return "false";
        }


    }

    @GetMapping("/getDiagnosis")
    public ResponseEntity<String> getAllDiagnosis() {
        String dig = doctorService.getAllDiagnosisJson();
        return ResponseEntity.ok(dig);
    }

    @PreAuthorize("hasAuthority('DOC')")
    @PostMapping("/getPatDiagnosis")
    public ResponseEntity<String> getPatientsByDiagnosis(@RequestBody String DiagnosisId) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(DiagnosisId);
            String diagnosisId = rootNode.path("DiagnosisId").asText();
            return ResponseEntity.ok(doctorService.getPatientsDiagnosedby(diagnosisId));
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    /*@PreAuthorize("hasAuthority('DOC')")
    @PostMapping("/getPatient")
    public String getPatient() {
        String patientData = null;

        return patientData;
    }*/

    @PreAuthorize("hasAuthority('DOC')")
    @PostMapping("/getDocPatients")
    public String getDocPatients(@RequestHeader("Authorization") String authHeader) {
        String docId = jwtService.getIDFromToken(authHeader.substring(7));
        return doctorService.getPatientsDoc(docId);
    }

    @PreAuthorize("hasAuthority('DOC')")
    @PostMapping("/diagnosePatient")
    public ResponseEntity<Boolean> diagnosPatient(@RequestBody String PatientJsonDiagnosis,@RequestHeader("Authorization") String authHeader) {
        String docId = jwtService.getIDFromToken(authHeader.substring(7));
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(PatientJsonDiagnosis);
            String diagnosisId = rootNode.path("DiagnosisId").asText();
            LocalDate diagnosisDate = LocalDate.parse(rootNode.path("DiagnosisDate").asText());
            String PatientId =  rootNode.path("PatientId").asText();

            return ResponseEntity.ok(doctorService.diagnosePatient(diagnosisId,diagnosisDate,docId,PatientId));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.ok(false);
        }
    }


    @GetMapping("/{id}")
    public Doctor getDoctorById(@PathVariable Long id) {
        return doctorService.getDoctorById(id);
    }



    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
    }

}