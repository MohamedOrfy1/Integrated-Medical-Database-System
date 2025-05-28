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
    @JoinColumn(name = "diagnosis_code", nullable = false, insertable = false, updatable = false)
    private Diagnosis diagnosis;

    @MapsId("patientId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false, insertable = false, updatable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    // Helper method to access diagnosis date
    public LocalDate getDiagnosisDate() {
        return id != null ? id.getDiagnosisDate() : null;
    }

    public void setDiagnosisDate(LocalDate diagnosisDate) {
        if (id == null) {
            id = new PatientDiagnosisId();
        }
        id.setDiagnosisDate(diagnosisDate);
    }
}