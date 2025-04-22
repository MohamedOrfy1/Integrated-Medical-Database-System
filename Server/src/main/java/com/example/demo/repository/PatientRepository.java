package com.example.demo.repository;

import com.example.demo.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    // Find by patient_id (custom ID)
    Optional<Patient> findByPatientId(String patientId);

    // Find by phone number
    Optional<Patient> findByPhoneNumber(String phoneNumber);

    // Find by email
    Optional<Patient> findByEmail(String email);

    // Search patients by name components
    @Query("SELECT p FROM Patient p WHERE " +
            "LOWER(p.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.fatherName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.grandfatherName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.familyName) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Patient> searchByName(@Param("query") String query);

    // Find patients registered between dates
    List<Patient> findByRegistrationDateBetween(LocalDate startDate, LocalDate endDate);

    // Find patients registered after a specific date
    List<Patient> findByRegistrationDateAfter(LocalDate date);

    // Check if patient exists by phone number
    boolean existsByPhoneNumber(String phoneNumber);

    // Check if patient exists by email
    boolean existsByEmail(String email);

    // Update patient email
    @Modifying
    @Query("UPDATE Patient p SET p.email = :email WHERE p.patientId = :patientId")
    int updateEmail(@Param("patientId") String patientId, @Param("email") String email);

    // Update patient phone number
    @Modifying
    @Query("UPDATE Patient p SET p.phoneNumber = :phoneNumber WHERE p.patientId = :patientId")
    int updatePhoneNumber(@Param("patientId") String patientId, @Param("phoneNumber") String phoneNumber);

    // Count patients registered on a specific date
    long countByRegistrationDate(LocalDate date);

    // Find patients with pagination support
    @Query("SELECT p FROM Patient p ORDER BY p.registrationDate DESC")
    List<Patient> findAllWithPagination(org.springframework.data.domain.Pageable pageable);

    @Modifying
    @Transactional
    @Query("INSERT INTO Patient(patientId, firstName, fatherName, grandfatherName, familyName, phoneNumber, registrationDate, email, name) " +
            "VALUES(:#{#patient.patientId}, :#{#patient.firstName}, :#{#patient.fatherName}, " +
            ":#{#patient.grandfatherName}, :#{#patient.familyName}, :#{#patient.phoneNumber}, " +
            ":#{#patient.registrationDate}, :#{#patient.email}, :#{#patient.name})")
    int insertPatient(@Param("patient") Patient patient);
}