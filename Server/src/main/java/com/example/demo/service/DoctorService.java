package com.example.demo.service;

import java.util.List;
import com.example.demo.model.Doctor;

public interface DoctorService {
    Doctor createDoctor(Doctor doctor);
    List<Doctor> getAllDoctors();
    Doctor getDoctorById(Long id);
    void deleteDoctor(Long id);
    boolean checkDoc(String user,String pass);
    String getDoctorsInJson();
}