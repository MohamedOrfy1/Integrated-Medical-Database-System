package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "department")
public class Department {
    @Id
    @Column(name = "dept_id", nullable = false, length = 3)
    private String deptId;

    @Column(name = "dept_name", nullable = false, length = 40)
    private String deptName;

}