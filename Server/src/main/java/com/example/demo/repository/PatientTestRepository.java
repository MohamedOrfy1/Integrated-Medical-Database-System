package com.example.demo.repository;

import com.example.demo.model.PatientTest;
import com.example.demo.model.PatientTestId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientTestRepository extends JpaRepository<PatientTest, PatientTestId> {

    // 1. Basic CRUD operations are inherited from JpaRepository

    // 2. Find all tests for a specific patient
    @Query("SELECT pt FROM PatientTest pt WHERE pt.id.patientId = :patientId")
    List<PatientTest> findAllByPatientId(@Param("patientId") String patientId);

    // 3. Get only test IDs for a specific patient (as requested)
    @Query("SELECT pt.id.testId FROM PatientTest pt WHERE pt.id.patientId = :patientId")
    List<Integer> findTestIdsByPatientId(@Param("patientId") String patientId);

    // 4. Find all patients for a specific test
    @Query("SELECT pt FROM PatientTest pt WHERE pt.id.testId = :testId")
    List<PatientTest> findAllByTestId(@Param("testId") Integer testId);

    // 5. Check if a specific patient-test combination exists
    boolean existsById_PatientIdAndId_TestId(String patientId, Integer testId);

    // 6. Find by composite ID components
    Optional<PatientTest> findById_PatientIdAndId_TestId(String patientId, Integer testId);

    // 7. Count all tests for a patient
    long countById_PatientId(String patientId);

    // 8. Delete all tests for a patient
    void deleteById_PatientId(String patientId);

    // 9. Delete specific patient-test combination
    void deleteById_PatientIdAndId_TestId(String patientId, Integer testId);

    // 10. Find with patient and test entities fetched eagerly
    @Query("SELECT pt FROM PatientTest pt JOIN FETCH pt.patient JOIN FETCH pt.test WHERE pt.id.patientId = :patientId")
    List<PatientTest> findAllByPatientIdWithDetails(@Param("patientId") String patientId);
}