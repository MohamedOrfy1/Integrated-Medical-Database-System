package com.example.JPAdemo.models;

import com.example.JPAdemo.SystemUser.Doctor;
import com.example.JPAdemo.SystemUser.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dept_id")
    private Long deptId;
    @Column(name = "dept_name", nullable = false, unique = true)
    private String deptName;
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Doctor> doctors = new ArrayList<>();
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Employee> employees = new ArrayList<>();

}
