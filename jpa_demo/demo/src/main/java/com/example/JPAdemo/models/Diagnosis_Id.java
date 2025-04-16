package com.example.JPAdemo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Setter
@Getter
@Embeddable
public class Diagnosis_Id implements Serializable {//Composite Key Class
    @Column(length = 14,insertable = false, updatable = false)
    private String national_id;
    @Column(name = "diagnosis_code", length = 25)
    private String diagnosis_code;
}
