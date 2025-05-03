package com.example.demo.service;

import com.example.demo.model.Employee;

public interface EmployeeService {
    boolean registerPatient(String employeeID,String patientID);
    boolean assignDocToPatient(String doctorID, String patientID);


}