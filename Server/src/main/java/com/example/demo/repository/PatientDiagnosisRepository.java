package com.example.demo.repository;

import com.example.demo.model.Patient;
import com.example.demo.model.PatientDiagnosisId;
import com.example.demo.model.PatientDiagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDate;

@Repository
public interface PatientDiagnosisRepository extends JpaRepository<PatientDiagnosis, PatientDiagnosisId> {

    // Basic CRUD operations are inherited from JpaRepository

    // Find all patient diagnoses by diagnosis code
    List<PatientDiagnosis> findByDiagnosisCode_DiagnosisCode(String diagnosisCode);

    // Find all patient diagnoses by patient ID
    List<PatientDiagnosis> findByPatient_PatientId(String patientId);

    // Find patient diagnoses by date range
    List<PatientDiagnosis> findByDiagnosisDateBetween(LocalDate startDate, LocalDate endDate);

    // Custom query to get all patients with a specific diagnosis
    @Query("SELECT pd.patient FROM PatientDiagnosis pd " +
            "WHERE pd.diagnosisCode.diagnosisCode = :diagnosisCode")
    List<Patient> findPatientsByDiagnosisCode(@Param("diagnosisCode") String diagnosisCode);

    // Alternative version with full patient data joined from PatientDiagnosis
    @Query("SELECT p FROM Patient p " +
            "JOIN PatientDiagnosis pd ON p.patientId = pd.patient.patientId " +
            "WHERE pd.diagnosisCode.diagnosisCode = :diagnosisCode")
    List<Patient> findPatientsWithDiagnosis(@Param("diagnosisCode") String diagnosisCode);

    // Find patient diagnoses with patient and diagnosis details
    @Query("SELECT pd FROM PatientDiagnosis pd " +
            "JOIN FETCH pd.patient " +
            "JOIN FETCH pd.diagnosisCode " +
            "WHERE pd.diagnosisCode.diagnosisCode = :diagnosisCode")
    List<PatientDiagnosis> findFullDiagnosisInfoByCode(@Param("diagnosisCode") String diagnosisCode);
}