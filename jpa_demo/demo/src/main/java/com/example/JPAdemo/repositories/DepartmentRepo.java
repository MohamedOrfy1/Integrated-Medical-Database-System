package com.example.JPAdemo.repositories;

import com.example.JPAdemo.SystemUser.Doctor;
import com.example.JPAdemo.models.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface DepartmentRepo extends JpaRepository<Department, Long> {
}
