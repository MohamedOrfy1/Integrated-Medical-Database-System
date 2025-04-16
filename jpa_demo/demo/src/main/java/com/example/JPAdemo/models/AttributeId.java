package com.example.JPAdemo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
@Embeddable
public class AttributeId implements Serializable {//Composite Key Class
    @Column(name = "test_id")
    private Long test_id;

    @Column(name = "attribute_id")
    private Integer attribute_id;
}
