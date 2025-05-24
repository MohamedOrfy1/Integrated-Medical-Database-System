package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "blood_test")
public class BloodTest {
    @Id
    @ColumnDefault("nextval('blood_test_test_id_seq')")
    @Column(name = "test_id", nullable = false)
    private Integer id;

    @Column(name = "test_type", nullable = false, length = 10)
    private String testType;

    @Column(name = "sample_date", nullable = false)
    private LocalDate sampleDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assisting_doctorid", nullable = false)
    private Doctor assistingDoctorid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewing_doctorid")
    private Doctor reviewingDoctorid;

    @Column(name = "printing_date")
    private LocalDate printingDate;

}