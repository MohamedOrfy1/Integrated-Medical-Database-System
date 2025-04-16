package com.example.JPAdemo.models;

import com.example.JPAdemo.SystemUser.Doctor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Getter
@Setter
@Entity
public class Admission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient visting_patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor Admitted_doctor;

    @Column(name = "admission_date", nullable = false)
    private LocalDate admissionDate;

    public Admission(Patient visting_patient, Doctor admitted_doctor, LocalDate admissionDate) {
        this.visting_patient = visting_patient;
        Admitted_doctor = admitted_doctor;
        this.admissionDate = admissionDate;
    }
}
