package com.example.demo.repository;

import com.example.demo.model.Admission;
import com.example.demo.model.AdmissionId;
import com.example.demo.model.Doctor;
import com.example.demo.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdmissionRepository extends JpaRepository<Admission, AdmissionId> {

    // Find admission by composite ID
    Optional<Admission> findById_PatientIdAndId_AdmissionDate(String patientId, LocalDate admissionDate);

    // Find all admissions for a specific patient
    List<Admission> findByPatient_PatientId(String patientId);


    @Query("SELECT a.patient FROM Admission a WHERE a.doctor.doctorId = :doctorId")
    List<Patient> findPatientsByDoctorId(@Param("doctorId") String doctorId);

    // Find all admissions by a specific doctor
    List<Admission> findByDoctor_DoctorId(String doctorId);

    // Find admissions between dates for a patient
    List<Admission> findByPatient_PatientIdAndId_AdmissionDateBetween(
            String patientId, LocalDate startDate, LocalDate endDate);

    // Find admissions on a specific date
    List<Admission> findById_AdmissionDate(LocalDate admissionDate);

    // Check if admission exists for patient on specific date
    boolean existsById_PatientIdAndId_AdmissionDate(String patientId, LocalDate admissionDate);

    // Count admissions by doctor
    long countByDoctor_DoctorId(String doctorId);

    // Custom query to find admissions with patient and doctor details
    @Query("SELECT a FROM Admission a JOIN FETCH a.patient p JOIN FETCH a.doctor d " +
            "WHERE a.id.admissionDate BETWEEN :startDate AND :endDate")
    List<Admission> findAdmissionsWithDetailsBetweenDates(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // Delete all admissions for a patient
    @Query("DELETE FROM Admission a WHERE a.patient.patientId = :patientId")
    int deleteByPatientId(@Param("patientId") String patientId);

    // Find latest admission for a patient
    @Query("SELECT a FROM Admission a WHERE a.patient.patientId = :patientId " +
            "ORDER BY a.id.admissionDate DESC LIMIT 1")
    Optional<Admission> findLatestAdmissionByPatient(@Param("patientId") String patientId);
}
