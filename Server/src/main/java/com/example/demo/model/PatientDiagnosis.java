package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "patient_diagnosis")
public class PatientDiagnosis {
    @EmbeddedId
    private PatientDiagnosisId id;

    @MapsId("diagnosisCode")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "diagnosis_code", nullable = false)
    private Diagnosis diagnosisCode;

    @MapsId("patientId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "diagnosis_date", nullable = false)
    private LocalDate diagnosisDate;

}