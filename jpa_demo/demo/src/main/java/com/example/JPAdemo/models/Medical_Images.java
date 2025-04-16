package com.example.JPAdemo.models;

import com.example.JPAdemo.SystemUser.Doctor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Setter
@Getter
@Entity
public class Medical_Images {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;
    @Column(name = "dicom_file_path",nullable = false)
    private String dicomFilePath;
    @Column(name = "scan_date",nullable = false)
    private LocalDate scanDate;
    @Column(name = "report_path",nullable = false)
    private String reportPath;
    @ManyToOne
    @JoinColumn(name = "patient_id",nullable = false)
    private Patient patient;
    @ManyToOne
    @JoinColumn(name = "doctor_id",nullable = false)
    private Doctor doctor;

}
