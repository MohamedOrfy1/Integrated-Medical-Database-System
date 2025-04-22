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
public class TestAttributeId implements Serializable {
    private static final long serialVersionUID = 127317245591651129L;
    @Column(name = "attribute_name", nullable = false)
    private String attributeName;

    @Column(name = "test_id", nullable = false)
    private Integer testId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TestAttributeId entity = (TestAttributeId) o;
        return Objects.equals(this.attributeName, entity.attributeName) &&
                Objects.equals(this.testId, entity.testId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attributeName, testId);
    }

}