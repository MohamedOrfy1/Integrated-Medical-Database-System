package com.example.JPAdemo.repositories;

import com.example.JPAdemo.models.Diagnosis;
import com.example.JPAdemo.models.Diagnosis_Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Diagnosis_repo extends JpaRepository<Diagnosis, Diagnosis_Id> {
}
