package com.example.JPAdemo.SystemUser;

import com.example.JPAdemo.models.Department;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Employee {
    @Id
    @Column(name = "national_id", length = 14, unique = true, nullable = false)
    private String national_id;
    @Column(nullable = false)
    private String first_name;
    @Column(nullable = false)
    private String last_name;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private MyUser employee;
    @ManyToOne
    @JoinColumn(name = "dept_id",nullable = false)
    private Department department;

}
