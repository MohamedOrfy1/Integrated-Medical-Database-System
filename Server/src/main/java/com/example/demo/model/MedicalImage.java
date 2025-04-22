package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "medical_images")
public class MedicalImage {
    @Id
    @ColumnDefault("nextval('medical_images_image_id_seq')")
    @Column(name = "image_id", nullable = false)
    private Integer id;

    @Column(name = "scan_date", nullable = false)
    private LocalDate scanDate;

    @Column(name = "dicom_path", nullable = false)
    private String dicomPath;

    @Column(name = "report_path")
    private String reportPath;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

}