package com.example.demo.repository;

import com.example.demo.model.Doctor;
import com.example.demo.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {


    // Find by doctor_id (your custom ID)
    Optional<Doctor> findByDoctorId(String doctorId);

    // Find by username
    Optional<Doctor> findByUsername(String username);

    // Find by email
    Optional<Doctor> findByEmail(String email);

    // Find doctors by department
    List<Doctor> findByDept(Department department);

    // Find doctors by department ID
    @Query("SELECT d FROM Doctor d WHERE d.dept.deptId = :deptId")
    List<Doctor> findByDeptId(@Param("deptId") String deptId);

    // Find doctors by specialty type
    List<Doctor> findByDocType(String docType);

    // Find doctors by name (first or last)
    @Query("SELECT d FROM Doctor d WHERE LOWER(d.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
            "LOWER(d.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Doctor> searchByName(@Param("name") String name);

    // Find active doctors (if you add an 'active' field later)
    // List<Doctor> findByActiveTrue();

    @Query("SELECT d FROM Doctor d WHERE d.username = :username AND d.password = :password")
    Optional<Doctor> findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    // Count doctors by department
    @Query("SELECT COUNT(d) FROM Doctor d WHERE d.dept.deptId = :deptId")
    long countByDeptId(@Param("deptId") String deptId);

    // Check if doctor exists by email
    boolean existsByEmail(String email);

    // Check if username exists
    boolean existsByUsername(String username);

    // Custom update for email
    @Modifying
    @Query("UPDATE Doctor d SET d.email = :email WHERE d.doctorId = :doctorId")
    int updateEmail(@Param("doctorId") String doctorId, @Param("email") String email);

    // Find doctors with department details (eager fetch example)
    @Query("SELECT d FROM Doctor d JOIN FETCH d.dept WHERE d.doctorId = :doctorId")
    Optional<Doctor> findByIdWithDepartment(@Param("doctorId") String doctorId);
}