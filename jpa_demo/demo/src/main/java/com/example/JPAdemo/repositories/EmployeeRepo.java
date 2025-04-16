package com.example.JPAdemo.repositories;

import com.example.JPAdemo.SystemUser.Doctor;
import com.example.JPAdemo.SystemUser.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, String> {

}
