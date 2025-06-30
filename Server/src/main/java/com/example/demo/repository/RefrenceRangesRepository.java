package com.example.demo.repository;

import com.example.demo.model.ReferenceRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefrenceRangesRepository extends JpaRepository<ReferenceRange, String> {

    // Find all reference ranges ordered by attribute name
    List<ReferenceRange> findAllByOrderByAttributeNameAsc();

    // Find reference ranges within a specific range value
    @Query("SELECT r FROM ReferenceRange r WHERE :value BETWEEN r.fromRange AND r.toRange")
    List<ReferenceRange> findByValueInRange(@Param("value") double value);

    List<ReferenceRange> findByUnit(String unit);

    // Find reference ranges where fromRange is greater than specified value
    List<ReferenceRange> findByFromRangeGreaterThan(double value);
}