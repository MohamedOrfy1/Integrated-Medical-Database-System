package com.example.JPAdemo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Patient {
    @Id
    @Column(name = "national_id", length = 14, unique = true, nullable = false)
    private String nationalId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "father_name", nullable = false)
    private String fatherName;

    @Column(name = "grandfather_name", nullable = false)
    private String grandfatherName;

    @Column(name = "family_name", nullable = false)
    private String familyName;
    @Column(name = "phone_number", length = 11,nullable = false)
    private String phoneNumber;
    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;
    @Transient
    private Integer age;
    @Transient
    private LocalDate dob;
    @Transient
    private String sex;
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Medical_Images> medicalImages = new ArrayList<>();
    @OneToMany(mappedBy = "patient_tested", cascade = CascadeType.ALL)
    @Column(nullable = false)
    private List<Blood_Test> blood_tests = new ArrayList<>();
    @OneToMany(mappedBy = "patient_diagnosed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Diagnosis> diagnoses = new ArrayList<>();
    @OneToMany(mappedBy = "visting_patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Admission> admissions = new ArrayList<>();
    public String getSex()
    {
        int genderDigit = Character.getNumericValue(nationalId.charAt(12));
        return (genderDigit % 2 == 1) ? "male" : "female";
    }
    // Calculate patient's DOB from the national id
    public LocalDate getDob()
    {
        char centuryCode = nationalId.charAt(0);
        String yymmdd = nationalId.substring(1, 7);
        int century;
        switch (centuryCode) {
            case '2': century = 1900; break;
            case '3': century = 2000; break;
            default: century = 1900;
        }
        int year = century + Integer.parseInt(yymmdd.substring(0, 2));
        int month = Integer.parseInt(yymmdd.substring(2, 4));
        int day = Integer.parseInt(yymmdd.substring(4, 6));
        return LocalDate.of(year, month, day);
    }
    //Calculate patient's age from the national id
    public Integer getAge()
    {
        LocalDate dob = getDob();
        if (dob == null) return null;

        return Period.between(dob, LocalDate.now()).getYears();
    }
}
