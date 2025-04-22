package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class PatientDiagnosisId implements Serializable {
    private static final long serialVersionUID = -4784497829028932209L;
    @Column(name = "diagnosis_code", nullable = false, length = 25)
    private String diagnosisCode;

    @Column(name = "patient_id", nullable = false, length = 14)
    private String patientId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PatientDiagnosisId entity = (PatientDiagnosisId) o;
        return Objects.equals(this.patientId, entity.patientId) &&
                Objects.equals(this.diagnosisCode, entity.diagnosisCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patientId, diagnosisCode);
    }

}