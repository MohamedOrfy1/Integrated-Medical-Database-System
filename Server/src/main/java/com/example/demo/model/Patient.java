package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "patient")
public class Patient {
    @Id
    @Column(name = "patient_id", nullable = false, length = 14)
    private String patientId;

    @Column(name = "first_name", nullable = false, length = 20)
    private String firstName;

    @Column(name = "father_name", nullable = false, length = 20)
    private String fatherName;

    @Column(name = "grandfather_name", nullable = false, length = 20)
    private String grandfatherName;

    @Column(name = "family_name", length = 20)
    private String familyName;

    @Column(name = "phone_number",unique = true, nullable = false, length = 11)
    private String phoneNumber;

    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;
    @Transient
    private Integer age;
    @Transient
    private String sex;

//    @ColumnDefault("nextval('patient_id_seq')")
//    @Column(name = "id", nullable = false)
//    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

}