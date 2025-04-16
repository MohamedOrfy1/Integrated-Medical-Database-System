package com.example.JPAdemo.models;

import jakarta.persistence.*;

@Entity
public class Test_attributes {
    @EmbeddedId
    private AttributeId attribute_id;
    @Column(name = "attribute_name", nullable = false)
    private String attributeName;
    @Column(name = "attribute_value", nullable = false)
    private Double attributeValue;//number
    @Column(name = "range_type", nullable = false)
    private String rangeType;
    @Column(name = "unit")
    private String unit;
    @ManyToOne
    @MapsId("test_id")
    @JoinColumn(name = "test_id")
    private Blood_Test bloodTest;
}
