package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "blood_test")
public class BloodTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name = "comments", nullable = true)
    private String comments;

}