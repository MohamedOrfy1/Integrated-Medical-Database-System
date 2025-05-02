package com.example.demo.repository;

import com.example.demo.model.Admission;
import com.example.demo.model.AdmissionId;
import com.example.demo.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdmissionRepository extends JpaRepository<Admission, AdmissionId> {
    @Query("SELECT a.patient FROM Admission a WHERE a.doctor.doctorId = :doctorId " +
            "AND a.patient.patientId = :nationalId")
    Optional<Patient> findPatientByDoctorAndNationalId(
            @Param("doctorId") String doctorId,
            @Param("nationalId") String nationalId);
    @Query("SELECT a FROM Admission a WHERE a.doctor.doctorId = :doctorId " +
            "ORDER BY a.id.admissionDate DESC")
    List<Admission> findAllByDoctorId(@Param("doctorId") String doctorId);
    
@Query("SELECT a FROM Admission a WHERE a.doctor.doctorId = :doctorId " +
        "AND (CAST(:startDate AS date) IS NULL OR a.id.admissionDate >= CAST(:startDate AS date)) " +
        "AND (CAST(:endDate AS date) IS NULL OR a.id.admissionDate <= CAST(:endDate AS date)) " +
        "ORDER BY a.id.admissionDate DESC")
    List<Admission> findByDoctorIdAndDateRange(
            @Param("doctorId") String doctorId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);


}