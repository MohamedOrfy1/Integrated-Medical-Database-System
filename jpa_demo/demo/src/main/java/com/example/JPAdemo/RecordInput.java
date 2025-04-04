package com.example.JPAdemo;

import com.example.JPAdemo.SystemUser.*;
import com.example.JPAdemo.repositories.DoctorRepo;
import com.example.JPAdemo.repositories.EmployeeRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
//----------------------to test with the inputs------------------
@Component
public class RecordInput implements CommandLineRunner {
    private final SystemUserRepo systemUserRepo;
    private final DoctorRepo doctorRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
private final EmployeeRepo EmpRepo;

    public RecordInput(SystemUserRepo systemUserRepo,DoctorRepo doctorRepo, BCryptPasswordEncoder bCryptPasswordEncoder,EmployeeRepo EmpRepo) {
        this.systemUserRepo = systemUserRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.doctorRepo = doctorRepo;
        this.EmpRepo=EmpRepo;
    }
    @Override
    public void run(String... args) throws Exception {
        // Check if the doctor already exists to avoid duplicate entries
        Doctor doctor = new Doctor();
        doctor.setNational_id("30208290201823"); // Set the national ID
        doctor.setFirst_name("John");
        doctor.setLast_name("Smith");
        Employee emp=new Employee();
        emp.setNational_id("12345678912345");
        emp.setFirst_name("salma");
        emp.setLast_name("yousry");

        // Create a new SystemUser
        MyUser systemUser = new MyUser();
        MyUser systemUser2=new MyUser();
        systemUser.setUsername("dr.smith");
        systemUser.setPassword(bCryptPasswordEncoder.encode("password")); // Encrypt the password
        systemUser.setRole(UserRole.ROLE_DOCTOR); // Set the role
        doctor.setUser(systemUser);
        systemUserRepo.save(systemUser);
        doctorRepo.save(doctor);
        systemUser2.setUsername("salma@emp");
        systemUser2.setPassword(bCryptPasswordEncoder.encode("password"));
        systemUser2.setRole(UserRole.ROLE_EMPLOYEE);
        emp.setEmployee(systemUser2);
        systemUserRepo.save(systemUser2);
        EmpRepo.save(emp);
    }
}
