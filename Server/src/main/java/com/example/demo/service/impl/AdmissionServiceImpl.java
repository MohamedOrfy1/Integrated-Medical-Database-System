package com.example.demo.service.impl;

import com.example.demo.DTO.AdmissionRequestDTO;
import com.example.demo.model.Admission;
import com.example.demo.model.AdmissionId;
import com.example.demo.model.Doctor;
import com.example.demo.model.Patient;
import com.example.demo.repository.AdmissionRepository;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.PatientRepository;
import com.example.demo.service.DoctorService;
import com.example.demo.service.PatientService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AdmissionServiceImpl{
    private final AdmissionRepository admissionRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;


    @Transactional
    public Admission createAdmission(AdmissionRequestDTO request) {
        // Verify patient exists and get the entity
        Patient patient = patientRepository.findByPatientId(request.getPatientId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Patient not found with ID: " + request.getPatientId()));

        // Verify doctor exists and get the entity
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Doctor not found with ID: " + request.getDoctorId()));

        // Set admission date to current date if not provided
        LocalDate admissionDate = request.getAdmissionDate() != null
                ? request.getAdmissionDate()
                : LocalDate.now();

        // Create composite ID
        AdmissionId admissionId = new AdmissionId();
        admissionId.setPatientId(patient.getPatientId());
        admissionId.setAdmissionDate(admissionDate);

        // Create and save admission
        Admission admission = new Admission();
        admission.setId(admissionId);
        admission.setPatient(patient);
        admission.setDoctor(doctor);

        return admissionRepository.save(admission);
    }
}
