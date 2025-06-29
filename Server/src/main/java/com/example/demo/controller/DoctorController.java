package com.example.demo.controller;

import com.example.demo.model.Diagnosis;
import com.example.demo.service.CommonService;
import com.example.demo.service.DoctorService;
import com.example.demo.service.impl.JWTServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import com.example.demo.model.Doctor;
import com.example.demo.service.PDFGenService;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;

@CrossOrigin(origins = {
        "https://imbdc.vercel.app/",
        "http://localhost:5173"
}, allowedHeaders = "*")
@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;
    private final CommonService commonService;
    private final PDFGenService pdfGenService;
    private final JWTServiceImpl jwtService;
    private  final String FILE_DIRECTORY = "src/main/java/com/example/demo/PDFDocs/";
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
    public String login(@RequestBody String doc) {
        JSONObject root = new JSONObject(doc);

        String username = root.getString("username");
        String password = root.getString("password");


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
    @PreAuthorize("hasAuthority('DOC')")
    @PostMapping("/getPatTestsIds")
    public ResponseEntity<String> getPatTestsIds(@RequestBody String PatientID) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(PatientID);
            String pt = rootNode.path("PatientID").asText();
            return ResponseEntity.ok(doctorService.getPatTestsIds(pt));
        } catch (Exception e) {
            System.out.println(e);
            return null;
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
//-----------------------------------------------
    @PreAuthorize("hasAuthority('DOC')")
    @PostMapping("/getReportTest")
    public ResponseEntity<Resource> getReportTest(@RequestBody Integer TestId)throws IOException {
        doctorService.genReport(TestId);

        Resource resource = new UrlResource(Paths.get(FILE_DIRECTORY).resolve("output.pdf").normalize().toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/pdf"))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


    //-----------------------------------------------

    @PreAuthorize("hasAuthority('DOC')")
    @PostMapping("/getPatient")
    public ResponseEntity<String> getPatient(@RequestBody String patientid) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(patientid);
            String pid = rootNode.path("PatientID").asText();
            return ResponseEntity.ok(doctorService.getPatientDataJson(pid));
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }



        @PreAuthorize("hasAuthority('DOC')")
        @PostMapping("/getDiagnosedPatients")
        public ResponseEntity<String> getDiagnosedPatients(@RequestBody String sortBy) {
        //System.out.println(sortBy);
        return ResponseEntity.ok(doctorService.getDiagnosedPatients(sortBy));
    }




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

    @PreAuthorize("hasAuthority('DOC')")
    @PostMapping("/addDiagnosis")
    public ResponseEntity<String> addDiagnosis(@RequestBody Diagnosis diagnosis) {
        try {
            if (doctorService.addDiagnosis(diagnosis.getDiagnosisCode(), diagnosis.getDiagnosisName())) {
                return ResponseEntity.ok("Diagnosis added successfully");
            } else {
                return ResponseEntity.badRequest().body("Diagnosis code already exists or invalid data");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error adding diagnosis");
        }
    }
    @PreAuthorize("hasAuthority('DOC')")
    @DeleteMapping("/diagnoses/{diagnosisCode}")
    public ResponseEntity<Boolean> deleteDiagnosis(@PathVariable String diagnosisCode) {
        try {
            boolean deleted = doctorService.deleteDiagnosis(diagnosisCode);
            return deleted
                    ? ResponseEntity.ok(true)
                    : ResponseEntity.ok(false);
        } catch (IllegalStateException e) {
            return ResponseEntity.ok(false);

        }
    }

    @PreAuthorize("hasAuthority('DOC')")
    @PostMapping("/insertest")
    public ResponseEntity<Boolean> insertest(@RequestBody String jsontest) {
        try {
                return ResponseEntity.ok(doctorService.InsertTest(jsontest)!=0?true:false);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }
    @PreAuthorize("hasAuthority('DOC')")
    @DeleteMapping("/deletest")
    public ResponseEntity<Boolean> deletest(@RequestBody String testid) {
        try {
            boolean deleted = doctorService.DeleteTest(Integer.parseInt(testid));
            return deleted
                    ? ResponseEntity.ok(true)
                    : ResponseEntity.ok(false);
        } catch (IllegalStateException e) {
            return ResponseEntity.ok(false);

        }
    }

    @PreAuthorize("hasAuthority('DOC')")
    @GetMapping("/getatts")
    public String getatts() { return doctorService.getallRefrenceRanges(); }



    @PreAuthorize("hasAuthority('DOC')")
    @PostMapping("/gengetest")
    public ResponseEntity<Resource> gengetest(@RequestBody String jsontest)throws IOException {

        Integer testId = doctorService.InsertTest(jsontest);
        doctorService.genReport(testId);

        Resource resource = new UrlResource(Paths.get(FILE_DIRECTORY).resolve("output.pdf").normalize().toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/pdf"))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }




}