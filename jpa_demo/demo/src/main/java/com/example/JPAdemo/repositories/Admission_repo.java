package com.example.JPAdemo.repositories;

import com.example.JPAdemo.models.Admission;
import com.example.JPAdemo.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface Admission_repo extends JpaRepository<Admission,Long> {

}
