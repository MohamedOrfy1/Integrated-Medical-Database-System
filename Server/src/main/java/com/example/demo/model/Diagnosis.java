package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "diagnosis")
public class Diagnosis {
    @Id
    @Column(name = "diagnosis_code", nullable = false, length = 25)
    private String diagnosisCode;

    @Column(name = "diagnosis_name", nullable = false)
    private String diagnosisName;

}