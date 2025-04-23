package com.example.demo.controller;

import com.example.demo.service.EmployeeService;
import com.example.demo.service.PDFGenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.model.Employee;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private String html =  "";




    private final EmployeeService employeeService;
    private static final String FILE_DIRECTORY = "src/main/java/com/example/demo/PDFDocs/" ;
    private final PDFGenService pdfGenService;

    @Autowired
    public EmployeeController(EmployeeService employeeService,PDFGenService pdfGenService) {

        this.employeeService = employeeService;
        this.pdfGenService = pdfGenService;
    }

    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeService.createEmployee(employee);
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return null;
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        return null;
    }


    //still needs extra work and modification
    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws IOException { //it downloads the file
        try {
            System.out.println(pdfGenService.convertXhtmlToPdf(html));
        }catch(Exception e){
            System.out.println("ERRORRR");
        }
        // Load file as Resource
        Path filePath = Paths.get(FILE_DIRECTORY).resolve(fileName).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        // Check if file exists
        if (!resource.exists()) {
            throw new RuntimeException("File not found: " + fileName);
        }

        // Determine content type
        String contentType = "application/pdf";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}