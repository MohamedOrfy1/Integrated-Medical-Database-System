package com.example.demo.service;

import com.example.demo.model.Employee;
import com.example.demo.model.Patient;

import java.time.LocalDate;

public interface EmployeeService {
    boolean registerPatient(String employeeID,String patientID);
    boolean assignDocToPatient(String doctorID, String patientID);
    String getPatientsByRegisterDateJson(LocalDate date);
    String getEmpID(String username,String password);
    Patient getPatientByID(String PatientID);


}