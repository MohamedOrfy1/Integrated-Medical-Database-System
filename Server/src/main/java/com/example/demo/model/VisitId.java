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
public class VisitId implements Serializable {
    private static final long serialVersionUID = -4000977694327755078L;
    @Column(name = "patient_id", nullable = false, length = 14)
    private String patientId;

    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        VisitId entity = (VisitId) o;
        return Objects.equals(this.patientId, entity.patientId) &&
                Objects.equals(this.registrationDate, entity.registrationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patientId, registrationDate);
    }

}