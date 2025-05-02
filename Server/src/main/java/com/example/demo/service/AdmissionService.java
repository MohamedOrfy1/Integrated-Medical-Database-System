package com.example.demo.service;

import com.example.demo.DTO.AdmissionRequestDTO;
import com.example.demo.model.Admission;

public interface AdmissionService {
    public Admission createAdmission(AdmissionRequestDTO request);
}
