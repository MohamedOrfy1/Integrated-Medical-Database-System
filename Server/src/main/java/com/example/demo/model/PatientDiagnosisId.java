package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class PatientDiagnosisId implements Serializable {
    private static final long serialVersionUID = -4784497829028932209L;

    @Column(name = "diagnosis_code", nullable = false, length = 10)
    private String diagnosisCode;

    @Column(name = "patient_id", nullable = false, length = 15)
    private String patientId;

    @Column(name = "diagnosis_date", nullable = false)
    private LocalDate diagnosisDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PatientDiagnosisId entity = (PatientDiagnosisId) o;
        return Objects.equals(this.patientId, entity.patientId) &&
                Objects.equals(this.diagnosisCode, entity.diagnosisCode) &&
                Objects.equals(this.diagnosisDate, entity.diagnosisDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patientId, diagnosisCode, diagnosisDate);
    }
}