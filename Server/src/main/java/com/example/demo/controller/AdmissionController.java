package com.example.demo.controller;

import com.example.demo.DTO.AdmissionRequestDTO;
import com.example.demo.model.Admission;
import com.example.demo.service.AdmissionService;
import com.example.demo.service.impl.AdmissionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;



@RestController
@RequestMapping("/admissions")
@RequiredArgsConstructor
public class AdmissionController {

    private final AdmissionServiceImpl admissionService;

    @PostMapping("/add")
    public ResponseEntity<Boolean> createAdmission(
           @RequestBody AdmissionRequestDTO request) {
        try {
            Admission admission = admissionService.createAdmission(request);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            //if duplicate record, OR patient/doctor ids don't exist
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Error creating admission: " + e.getMessage(),
                    e);
        }
    }

}
