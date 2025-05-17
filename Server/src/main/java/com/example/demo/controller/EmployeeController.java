package com.example.demo.controller;

import com.example.demo.service.EmployeeService;
import com.example.demo.service.PDFGenService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.UrlResource;
import java.time.LocalDate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.model.Employee;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private  final String FILE_DIRECTORY = "Server/src/main/java/com/example/demo/PDFDocs/" ;
    private String html =  "";




    private final EmployeeService employeeService;
    private final PDFGenService pdfGenService;

    @Autowired
    public EmployeeController(EmployeeService employeeService,PDFGenService pdfGenService) {

        this.employeeService = employeeService;
        this.pdfGenService = pdfGenService;
    }

    @PostMapping("/getPatDate")
    public ResponseEntity<String> getPatientsByDate(@RequestBody String registerDate) {
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            JsonNode rootNode = objectMapper.readTree(registerDate);
            String date = rootNode.path("Date").asText();
            return ResponseEntity.ok(employeeService.getPatientsByRegisterDateJson(LocalDate.parse(date))); //yyyy-MM-dd
        }catch(Exception e){
            System.out.println(e);
            return null;
        }



    }


    @GetMapping
    public List<Employee> getAllEmployees() {
        return null;
    }

    @PostMapping("/assign")
    @Transactional
    public ResponseEntity<Boolean> assignDocToPatient(@RequestBody String assignJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(assignJson);
            String patientId = rootNode.path("PatientId").asText();
            String doctorId = rootNode.path("DoctorId").asText();

            boolean isCorrect = employeeService.assignDocToPatient(doctorId,patientId);
            if (isCorrect){
                return ResponseEntity.ok(true);
            }else{
                return ResponseEntity.ok(false);
            }

        }catch(Exception e){
            System.out.println(e);
            return ResponseEntity.ok(false);
        }

    }





    @PostMapping("/genReport")
    public ResponseEntity<Resource> downloadFile(@RequestBody String ReportJson) throws IOException { //it downloads the file
        try {

            pdfGenService.convertXhtmlToPdf(pdfGenService.replacePlaceholders(ReportJson));
        }catch(Exception e){
        }

        Resource resource = new UrlResource(Paths.get(FILE_DIRECTORY).resolve("output.pdf").normalize().toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/pdf"))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}