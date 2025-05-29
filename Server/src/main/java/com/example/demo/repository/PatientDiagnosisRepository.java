package com.example.demo.repository;

import com.example.demo.DTO.DiagnosisDTO;
import com.example.demo.model.Patient;
import com.example.demo.model.PatientDiagnosis;
import com.example.demo.model.PatientDiagnosisId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PatientDiagnosisRepository extends JpaRepository<PatientDiagnosis, PatientDiagnosisId> {

    // Find all patient diagnoses for a given diagnosis code
    List<PatientDiagnosis> findByDiagnosis_DiagnosisCode(String diagnosisCode);

    // Find all patient diagnoses for a given patient ID
    List<PatientDiagnosis> findByPatient_PatientId(String patientId);

    // Find all patient diagnoses within a date range
    List<PatientDiagnosis> findById_DiagnosisDateBetween(LocalDate start, LocalDate end);

    // Find patients with a specific diagnosis code
    @Query("SELECT pd.patient FROM PatientDiagnosis pd WHERE pd.diagnosis.diagnosisCode = :diagnosisCode")
    List<Patient> findPatientsByDiagnosisCode(@Param("diagnosisCode") String diagnosisCode);

    // Alternative version using explicit JOIN
    @Query("SELECT p FROM Patient p " +
            "JOIN PatientDiagnosis pd ON p.patientId = pd.patient.patientId " +
            "WHERE pd.diagnosis.diagnosisCode = :diagnosisCode")
    List<Patient> findPatientsWithDiagnosis(@Param("diagnosisCode") String diagnosisCode);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO patient_diagnosis (patient_id, diagnosis_code, diagnosis_date, doctor_id) " +
            "VALUES (:patientId, :diagnosisCode, :diagnosisDate, :doctorId)", nativeQuery = true)
    int insertPatientDiagnosis(
            @Param("patientId") String patientId,
            @Param("diagnosisCode") String diagnosisCode,
            @Param("diagnosisDate") LocalDate diagnosisDate,
            @Param("doctorId") String doctorId);

    // Get full diagnosis info including associated patient and diagnosis
    @Query("SELECT pd FROM PatientDiagnosis pd " +
            "JOIN FETCH pd.patient " +
            "JOIN FETCH pd.diagnosis " +
            "WHERE pd.diagnosis.diagnosisCode = :diagnosisCode")
    List<PatientDiagnosis> findFullDiagnosisInfoByCode(@Param("diagnosisCode") String diagnosisCode);


        @Query("SELECT NEW com.example.demo.DTO.DiagnosisDTO(" +
                "doc.name, d.diagnosisName, pd.id.diagnosisDate) " +
                "FROM PatientDiagnosis pd " +
                "JOIN pd.diagnosis d " +
                "JOIN pd.doctor doc " +
                "WHERE pd.id.patientId = :patientId " +
                "ORDER BY pd.id.diagnosisDate DESC")
        List<DiagnosisDTO> findDiagnosisDetailsByPatientId(@Param("patientId") String patientId);

}