package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "reference_ranges")
public class ReferenceRange {
    @Id
    @Column(name = "attribute_name", nullable = false, length = 20)
    private String attributeName;

    @Column(name = "unit", nullable = false, length = 5)
    private String unit;

    @Column(name = "from_range", nullable = false)
    private Integer fromRange;

    @Column(name = "to_range", nullable = false)
    private Integer toRange;

}