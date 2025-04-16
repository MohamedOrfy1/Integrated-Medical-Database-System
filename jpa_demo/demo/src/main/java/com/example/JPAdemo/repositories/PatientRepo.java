package com.example.JPAdemo.repositories;

import com.example.JPAdemo.SystemUser.Employee;
import com.example.JPAdemo.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepo extends JpaRepository<Patient, String> {
}
