package com.example.JPAdemo.SystemUser;

import com.example.JPAdemo.models.Admission;
import com.example.JPAdemo.models.Blood_Test;
import com.example.JPAdemo.models.Department;
import com.example.JPAdemo.models.Medical_Images;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
public class Doctor  {
    @Id
    @Column(name = "national_id", length = 14, unique = true, nullable = false)
    private String national_id;
    @Column(nullable = false)
    private String first_name;
    @Column(nullable = false)
    private String last_name;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private MyUser user;
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Medical_Images> medicalImages = new ArrayList<>();
    @ManyToOne
  @JoinColumn(name = "dept_id",nullable = false)
    private Department department;
    @OneToMany(mappedBy = "assisted_doctor", cascade = CascadeType.ALL)
    private List<Blood_Test> blood_tests = new ArrayList<>();
    @OneToMany(mappedBy = "Admitted_doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Admission> admissions = new ArrayList<>();
}
