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

    @Query("SELECT pt.id.testId FROM PatientTest pt WHERE pt.id.patientId = :patientId")
    List<Integer> findTestIdsByPatientId(@Param("patientId") String patientId);


    // 10. Find with patient and test entities fetched eagerly
    @Query("SELECT pt FROM PatientTest pt JOIN FETCH pt.patient JOIN FETCH pt.test WHERE pt.id.patientId = :patientId")
    List<PatientTest> findAllByPatientIdWithDetails(@Param("patientId") String patientId);
}