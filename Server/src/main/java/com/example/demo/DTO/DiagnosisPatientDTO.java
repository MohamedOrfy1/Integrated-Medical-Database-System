package com.example.demo.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DiagnosisPatientDTO {
    private String patientId;
    private String diagnosisName;
    private LocalDate diagnosisDate;

    public DiagnosisPatientDTO(String patientID, String diagnosisName, LocalDate diagnosisDate) {
        this.patientId = patientID;
        this.diagnosisName = diagnosisName;
        this.diagnosisDate = diagnosisDate;
    }
}