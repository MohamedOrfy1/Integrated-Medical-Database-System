package com.example.JPAdemo.models;

import com.example.JPAdemo.SystemUser.Doctor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class Blood_Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long test_id;
    @Column(name = "test_type",nullable = false)
    private String test_type;
    @Column(name = "sample_date",nullable = false)
    private LocalDate sample_date;
    @Column(name = "printing_date",nullable = false)
    private LocalDate printing_date;
    private String comments;

    @OneToMany(mappedBy = "bloodTest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Test_attributes> testAttributes = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "patient_id",nullable = false)
    private Patient patient_tested;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Doctor assisted_doctor;

}
