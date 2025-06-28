package com.example.demo.repository;

import com.example.demo.model.TestAttribute;
import com.example.demo.DTO.BloodTestAttributeDTO;
import com.example.demo.model.BloodTest;
import com.example.demo.model.ReferenceRange;
import com.example.demo.model.TestAttributeId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestAttributeRepository extends JpaRepository<TestAttribute, TestAttributeId> {

    // Find all attributes for a specific test
    List<TestAttribute> findByTest(BloodTest test);

    // Find all attributes for a specific test ID
    List<TestAttribute> findByTestId(Integer testId);

    // Find a specific attribute for a test
    TestAttribute findByTestAndId_AttributeReference(BloodTest test, ReferenceRange attributeReference);

    // Custom query to get test attributes with reference ranges in one query
    @Query("SELECT ta, rr FROM TestAttribute ta " +
            "JOIN ta.id.attributeReference rr " +
            "WHERE ta.test.id = :testId")
    List<Object[]> findTestAttributesWithRanges(@Param("testId") Integer testId);

    // Query to get specific attributes with their reference ranges
    @Query("SELECT ta.attributeValue, rr.attributeName, rr.unit, rr.fromRange, rr.toRange " +
            "FROM TestAttribute ta " +
            "JOIN ta.id.attributeReference rr " +
            "WHERE ta.test.id = :testId " +
            "AND rr.attributeName IN :attributeNames")
    List<Object[]> findSpecificAttributesWithRanges(
            @Param("testId") Integer testId,
            @Param("attributeNames") List<String> attributeNames);

    // Query to get abnormal results (outside reference ranges)
    @Query("SELECT ta FROM TestAttribute ta " +
            "JOIN ta.id.attributeReference rr " +
            "WHERE ta.test.id = :testId " +
            "AND (ta.attributeValue < rr.fromRange OR ta.attributeValue > rr.toRange)")
    List<TestAttribute> findAbnormalResults(@Param("testId") Integer testId);

    // Count abnormal results for a test
    @Query("SELECT COUNT(ta) FROM TestAttribute ta " +
            "JOIN ta.id.attributeReference rr " +
            "WHERE ta.test.id = :testId " +
            "AND (ta.attributeValue < rr.fromRange OR ta.attributeValue > rr.toRange)")
    long countAbnormalResults(@Param("testId") Integer testId);

    // Query to get test attributes with reference ranges as DTO projection
    @Query("SELECT NEW com.example.demo.DTO.BloodTestAttributeDTO(" +
            "rr.attributeName, ta.attributeValue, rr.unit, rr.fromRange, rr.toRange) " +
            "FROM TestAttribute ta " +
            "JOIN ta.id.attributeReference rr " +
            "JOIN ta.test t " +
            "WHERE t.id = :testId")
    List<BloodTestAttributeDTO> findTestAttributesWithRangesAsDTO(@Param("testId") Integer testId);


    @Modifying
    @Transactional
    @Query("DELETE FROM TestAttribute ta WHERE ta.test.id = :testId")
    void deleteAllByTestId(@Param("testId") Integer testId);
}