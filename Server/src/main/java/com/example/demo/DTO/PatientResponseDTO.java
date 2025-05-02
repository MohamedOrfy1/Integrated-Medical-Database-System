package com.example.demo.DTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PatientResponseDTO {
    private String PatientId;
    private String name;
    private LocalDate admissionDate;
    private int age;
    private String gender;
}
