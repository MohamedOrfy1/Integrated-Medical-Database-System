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
public class AdmissionId implements Serializable {
    private static final long serialVersionUID = 909741216170321424L;
    @Column(name = "patient_id", nullable = false, length = 14)
    private String patientId;

    @Column(name = "admission_date", nullable = false)
    private LocalDate admissionDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AdmissionId entity = (AdmissionId) o;
        return Objects.equals(this.admissionDate, entity.admissionDate) &&
                Objects.equals(this.patientId, entity.patientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(admissionDate, patientId);
    }

}