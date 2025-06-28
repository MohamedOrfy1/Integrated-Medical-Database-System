package com.example.demo.repository;

import com.example.demo.DTO.TestHeaderInfoDTO;
import com.example.demo.model.BloodTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BloodTestRepository extends JpaRepository<BloodTest, Integer> {

    // Basic CRUD operations are inherited from JpaRepository

    // Find tests by test type
    List<BloodTest> findByTestType(String testType);

    // Find tests between dates
    List<BloodTest> findBySampleDateBetween(LocalDate startDate, LocalDate endDate);

    // Find tests by assisting doctor
    List<BloodTest> findByAssistingDoctorid_Id(Integer doctorId);

    // Find tests by reviewing doctor
    List<BloodTest> findByReviewingDoctorid_Id(Integer doctorId);

    // Find tests that haven't been printed yet
    List<BloodTest> findByPrintingDateIsNull();

    // Find tests that have been printed
    List<BloodTest> findByPrintingDateIsNotNull();

    // Custom query to find tests with specific criteria
    @Query("SELECT bt FROM BloodTest bt " +
            "WHERE bt.testType = :testType " +
            "AND bt.sampleDate BETWEEN :startDate AND :endDate " +
            "AND bt.printingDate IS NOT NULL")
    List<BloodTest> findPrintedTestsByTypeAndDateRange(
            @Param("testType") String testType,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // Count tests by type
    @Query("SELECT bt.testType, COUNT(bt) FROM BloodTest bt GROUP BY bt.testType")
    List<Object[]> countTestsByType();

    // Find tests with doctor details (join fetch example)
    @Query("SELECT bt FROM BloodTest bt " +
            "LEFT JOIN FETCH bt.assistingDoctorid " +
            "LEFT JOIN FETCH bt.reviewingDoctorid " +
            "WHERE bt.id = :testId")
    BloodTest findTestWithDoctors(@Param("testId") Integer testId);

    // Find tests that need review (no reviewing doctor assigned)
    @Query("SELECT bt FROM BloodTest bt WHERE bt.reviewingDoctorid IS NULL")
    List<BloodTest> findTestsNeedingReview();

    // Find tests by assisting doctor's name
    @Query("SELECT bt FROM BloodTest bt JOIN bt.assistingDoctorid d " +
            "WHERE LOWER(d.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<BloodTest> findByAssistingDoctorName(@Param("name") String name);

    @Query("SELECT NEW com.example.demo.DTO.TestHeaderInfoDTO(" +
            "bt.id, " +
            "bt.testType, " +
            "bt.sampleDate, " +
            "bt.printingDate, " +
            "p.patientId, " +
            "CONCAT(p.firstName, ' ', p.fatherName, ' ', p.grandfatherName, ' ', p.familyName), " +
            "CONCAT(ad.firstName, ' ', ad.lastName), " +
            "CONCAT(rd.firstName, ' ', rd.lastName), " +
            "bt.comments) " +
            "FROM PatientTest pt " +
            "JOIN pt.test bt " +
            "JOIN pt.patient p " +
            "LEFT JOIN bt.assistingDoctorid ad " +
            "LEFT JOIN bt.reviewingDoctorid rd " +
            "WHERE bt.id = :testId")
    TestHeaderInfoDTO findBloodTestReportByTestId(@Param("testId") Integer testId);


}