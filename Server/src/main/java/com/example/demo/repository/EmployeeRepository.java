package com.example.demo.repository;

import com.example.demo.model.Employee;
import com.example.demo.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

    // Find by employee_id (custom ID)
    Optional<Employee> findByEmployeeId(String employeeId);

    // Find by username
    Optional<Employee> findByUsername(String username);

    // Find employees by department
    List<Employee> findByDept(Department department);

    // Find employees by department ID
    @Query("SELECT e FROM Employee e WHERE e.dept.deptId = :deptId")
    List<Employee> findByDeptId(@Param("deptId") String deptId);

    // Search employees by name (first or last)
    @Query("SELECT e FROM Employee e WHERE " +
            "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
            "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Employee> searchByName(@Param("name") String name);

    // Check if username exists
    boolean existsByUsername(String username);

    // Update employee password
    @Modifying
    @Query("UPDATE Employee e SET e.password = :password WHERE e.employeeId = :employeeId")
    int updatePassword(@Param("employeeId") String employeeId, @Param("password") String password);

    // Find employee with department details (eager fetch)
    @Query("SELECT e FROM Employee e JOIN FETCH e.dept WHERE e.employeeId = :employeeId")
    Optional<Employee> findByIdWithDepartment(@Param("employeeId") String employeeId);

    // Find all employees with department details (for reporting)
    @Query("SELECT e FROM Employee e JOIN FETCH e.dept")
    List<Employee> findAllWithDepartment();
}