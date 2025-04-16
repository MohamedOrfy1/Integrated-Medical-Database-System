package com.example.JPAdemo;

import com.example.JPAdemo.SystemUser.*;
import com.example.JPAdemo.models.*;
import com.example.JPAdemo.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

//----------------------to test with the inputs------------------
@Component
public class RecordInput implements CommandLineRunner {
    private final SystemUserRepo systemUserRepo;
    private final DoctorRepo doctorRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmployeeRepo EmpRepo;
    private final DepartmentRepo dep_repo;
    private final PatientRepo patient_repo;
    private final MedicalImages_repo imgs_repo;
    private final Blood_test_repo blood_repo;
    private final Admission_repo admission_repo;
    private final Diagnosis_repo diagnosis_repo;



    public RecordInput(SystemUserRepo systemUserRepo,DoctorRepo doctorRepo, BCryptPasswordEncoder bCryptPasswordEncoder,EmployeeRepo EmpRepo,DepartmentRepo dep_repo,PatientRepo patient_repo,MedicalImages_repo imgs_repo,Blood_test_repo blood_repo,Admission_repo admission_repo,Diagnosis_repo diagnosis_repo) {
        this.systemUserRepo = systemUserRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.doctorRepo = doctorRepo;
        this.EmpRepo=EmpRepo;
        this.dep_repo=dep_repo;
        this.patient_repo=patient_repo;
        this.imgs_repo=imgs_repo;
        this.blood_repo=blood_repo;
        this.admission_repo=admission_repo;
        this.diagnosis_repo=diagnosis_repo;

    }
    @Override
    public void run(String... args) throws Exception {
        Department department=new Department();
        department.setDeptName("Radiology");
        dep_repo.save(department);
        // Check if the doctor already exists to avoid duplicate entries
        Doctor doctor = new Doctor();
        doctor.setNational_id("30208290201823"); // Set the national ID
        doctor.setFirst_name("John");
        doctor.setLast_name("Smith");
        doctor.setDepartment(department);
        Employee emp=new Employee();
        emp.setNational_id("12345678912345");
        emp.setFirst_name("salma");
        emp.setLast_name("yousry");
emp.setDepartment(department);
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
        Patient patient=new Patient();
        patient.setFirstName("Aly");
        patient.setFatherName("Elsayed");
        patient.setGrandfatherName("Aly");
        patient.setFamilyName("abdelaal");
        patient.setNationalId("30201107153559");
        patient.setPhoneNumber("01002014587");
        patient.setRegistrationDate(LocalDate.now());
        patient_repo.save(patient);
        Admission adm1=new Admission(patient,doctor,LocalDate.now());
        admission_repo.save(adm1);
        Medical_Images imgs=new Medical_Images();
        imgs.setScanDate(LocalDate.now());
        imgs.setReportPath("D:/local/reports/report_1.pdf");
        imgs.setDoctor(doctor);
        imgs.setDicomFilePath("D:/local/dicomFiles/img.dcm");
        imgs.setPatient(patient);
        imgs_repo.save(imgs);
        //adding patient diagnosis
        Diagnosis diagnosis = new Diagnosis();
        Diagnosis_Id diagnosisId = new Diagnosis_Id();
        diagnosisId.setNational_id(patient.getNationalId());
        diagnosisId.setDiagnosis_code("C95.9");
        diagnosis.setDiagnosis_id(diagnosisId);
        diagnosis.setDiagnosis_name("Sample Diagnosis");
        diagnosis.setDiagnosis_date(LocalDate.now());
diagnosis.setDiagnosis_name("Leukemia");
        diagnosis.setPatient_diagnosed(patient);
        diagnosis_repo.save(diagnosis);
    }
}
