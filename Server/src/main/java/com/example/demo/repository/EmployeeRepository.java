package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Doctor;

public interface EmployeeRepository extends JpaRepository<Doctor, Long> {
}