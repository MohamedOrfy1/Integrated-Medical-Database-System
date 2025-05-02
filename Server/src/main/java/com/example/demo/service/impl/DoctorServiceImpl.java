package com.example.demo.service.impl;

import com.example.demo.repository.DoctorRepository;
import com.example.demo.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import  com.example.demo.model.*;
import com.example.demo.service.DoctorService;

import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public Doctor createDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public Doctor getDoctorById(String id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void deleteDoctor(String id) {
        doctorRepository.deleteById(id);
    }

    @Override
    public boolean checkDoc(String user,String pass){
        return false;
    }
}