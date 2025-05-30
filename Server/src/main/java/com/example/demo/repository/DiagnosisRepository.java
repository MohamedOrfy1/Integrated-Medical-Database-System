package com.example.demo.repository;

import com.example.demo.model.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiagnosisRepository extends JpaRepository<Diagnosis, String> {

    // Basic CRUD operations are provided by JpaRepository

    // Find diagnosis by exact name
    Diagnosis findByDiagnosisName(String diagnosisName);

    // Find diagnoses containing the given name (case-insensitive)
    List<Diagnosis> findByDiagnosisNameContainingIgnoreCase(String namePart);

    // Get all diagnosis names only
    @Query("SELECT d.diagnosisName FROM Diagnosis d ORDER BY d.diagnosisName")
    List<String> findAllDiagnosisNames();

    // Check if a diagnosis exists with the given name
    boolean existsByDiagnosisName(String diagnosisName);

    // Count diagnoses containing the given name part
    long countByDiagnosisNameContaining(String namePart);

    // Find diagnoses ordered by name
    List<Diagnosis> findAllByOrderByDiagnosisNameAsc();

    boolean existsByDiagnosisCode(String diagnosisCode);

    @Modifying
    @Query("DELETE FROM Diagnosis d WHERE d.diagnosisCode = :code")
    int deleteByDiagnosisCode(@Param("code") String code);

    @Query(value = "SELECT COUNT(*) FROM patient_diagnosis WHERE diagnosis_code = :code", nativeQuery = true)
    int isDiagnosisInUse(@Param("code") String code);

}