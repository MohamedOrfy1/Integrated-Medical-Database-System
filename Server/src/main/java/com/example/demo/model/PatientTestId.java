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
public class PatientTestId implements Serializable {
    private static final long serialVersionUID = 413310815554188112L;
    @Column(name = "test_id", nullable = false)
    private Integer testId;

    @Column(name = "patient_id", nullable = false, length = 14)
    private String patientId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PatientTestId entity = (PatientTestId) o;
        return Objects.equals(this.patientId, entity.patientId) &&
                Objects.equals(this.testId, entity.testId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patientId, testId);
    }

}