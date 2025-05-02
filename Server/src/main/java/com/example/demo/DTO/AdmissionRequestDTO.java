package com.example.demo.DTO;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AdmissionRequestDTO {
    private String patientId;
    private String doctorId;
    private LocalDate admissionDate;
}
