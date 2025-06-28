package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "test_attributes")
public class TestAttribute {
    @EmbeddedId
    private TestAttributeId id;

    @MapsId("testId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "test_id", nullable = false)
    private BloodTest test;

    @Column(name = "attribute_value", columnDefinition = "numeric(6,3)")
    private double attributeValue;

}