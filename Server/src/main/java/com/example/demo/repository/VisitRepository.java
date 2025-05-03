package com.example.demo.repository;

import com.example.demo.model.Visit;
import com.example.demo.model.VisitId;
import com.example.demo.model.Patient;
import com.example.demo.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VisitRepository extends JpaRepository<Visit, VisitId> {

    // Basic CRUD operations are inherited from JpaRepository

    // Find by composite ID
    Optional<Visit> findById_PatientIdAndId_RegistrationDate(String patientId, LocalDate registrationDate);

    // Find all visits for a specific patient
    List<Visit> findByPatient_PatientId(String patientId);

    // Find all visits handled by a specific employee
    List<Visit> findByEmployee_EmployeeId(String employeeId);

    // Find visits between dates for a patient
    List<Visit> findByPatient_PatientIdAndId_RegistrationDateBetween(
            String patientId, LocalDate startDate, LocalDate endDate);

    // Find visits on a specific date
    List<Visit> findById_RegistrationDate(LocalDate registrationDate);

    // Check if visit exists for patient on specific date
    boolean existsById_PatientIdAndId_RegistrationDate(String patientId, LocalDate registrationDate);

    // Count visits by employee
    long countByEmployee_EmployeeId(String employeeId);

    // Custom query to find visits with patient and employee details
    @Query("SELECT v FROM Visit v JOIN FETCH v.patient p JOIN FETCH v.employee e " +
            "WHERE v.id.registrationDate BETWEEN :startDate AND :endDate")
    List<Visit> findVisitsWithDetailsBetweenDates(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // Find visits by patient with pagination
    @Query("SELECT v FROM Visit v WHERE v.patient.patientId = :patientId ORDER BY v.id.registrationDate DESC")
    List<Visit> findByPatientIdWithPagination(
            @Param("patientId") String patientId,
            org.springframework.data.domain.Pageable pageable);

    // Find latest visit for a patient
    @Query("SELECT v FROM Visit v WHERE v.patient.patientId = :patientId " +
            "ORDER BY v.id.registrationDate DESC LIMIT 1")
    Optional<Visit> findLatestVisitByPatient(@Param("patientId") String patientId);

    // Delete all visits for a patient
    @Modifying
    @Transactional
    @Query("DELETE FROM Visit v WHERE v.patient.patientId = :patientId")
    int deleteByPatientId(@Param("patientId") String patientId);
}