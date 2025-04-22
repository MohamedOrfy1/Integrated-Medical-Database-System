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
public class PatientImageId implements Serializable {
    private static final long serialVersionUID = 37829099942990197L;
    @Column(name = "image_id", nullable = false)
    private Integer imageId;

    @Column(name = "patient_id", nullable = false, length = 14)
    private String patientId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PatientImageId entity = (PatientImageId) o;
        return Objects.equals(this.imageId, entity.imageId) &&
                Objects.equals(this.patientId, entity.patientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageId, patientId);
    }

}