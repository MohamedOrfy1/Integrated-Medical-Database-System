package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class TestAttributeId implements Serializable {
    private static final long serialVersionUID = 127317245591651129L;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute_name", referencedColumnName = "attribute_name", nullable = false)
    private ReferenceRange attributeReference;

    @Column(name = "test_id", nullable = false)
    private Integer testId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TestAttributeId entity = (TestAttributeId) o;
        return Objects.equals(this.attributeReference, entity.attributeReference) &&
                Objects.equals(this.testId, entity.testId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attributeReference, testId);
    }
}