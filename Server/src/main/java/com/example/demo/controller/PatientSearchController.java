package com.example.demo.controller;

import com.example.demo.DTO.PatientResponseDTO;
import com.example.demo.model.Patient;
import com.example.demo.service.DoctorService;
import com.example.demo.service.impl.PatientSearchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/doctor/patients")
public class PatientSearchController {

    private final PatientSearchServiceImpl patientSearchService;
    private final DoctorService doctorService;

    @Autowired
    public PatientSearchController(PatientSearchServiceImpl patientSearchService,
                                   DoctorService doctorService) {
        this.patientSearchService = patientSearchService;
        this.doctorService = doctorService;

    }
    //doctor search with patient id, it should return patient infos and tests or medical images
    //not completed
    @GetMapping("/{doctorId}/search")
    public ResponseEntity<?> searchAdmittedPatient(
            @PathVariable String doctorId,
            @RequestParam String nationalId) {
        Optional<Patient> patient = patientSearchService.searchAdmittedPatients(doctorId, nationalId);
        return patient.isPresent()
                ? ResponseEntity.ok(patient.get())
                : ResponseEntity.notFound().build();
    }

    //List patients based on filters(age range/ admission date)
    @GetMapping("/{doctorId}/filter")
    public List<PatientResponseDTO> filterPatients(
            @PathVariable String doctorId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge) {

        return patientSearchService.searchPatientsWithFilters(
                doctorId,
                startDate,
                endDate,
                minAge,
                maxAge);
    }


}
