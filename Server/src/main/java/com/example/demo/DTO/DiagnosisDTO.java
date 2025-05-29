package com.example.demo.DTO;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiagnosisDTO {
    private String doctorName;
    private String diagnosisName;
    private LocalDate diagnosisDate;
    public DiagnosisDTO(String doctorName, String diagnosisName, LocalDate diagnosisDate) {
        this.doctorName = doctorName;
        this.diagnosisName = diagnosisName;
        this.diagnosisDate = diagnosisDate;
    }
}