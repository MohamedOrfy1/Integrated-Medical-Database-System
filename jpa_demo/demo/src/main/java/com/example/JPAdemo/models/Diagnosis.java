package com.example.JPAdemo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@Entity
public class Diagnosis {
    @EmbeddedId
    private Diagnosis_Id diagnosis_id;
    @Column(name = "diagnosis_name", nullable = false)
    private String diagnosis_name;
    @Column(name = "diagnosis_date", nullable = false)
    private LocalDate diagnosis_date;
    @ManyToOne
    @MapsId("national_id")
    @JoinColumn(nullable = false)
    private Patient patient_diagnosed;
}
