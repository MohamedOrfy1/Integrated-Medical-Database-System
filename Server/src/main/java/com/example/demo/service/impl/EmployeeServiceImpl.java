package com.example.demo.service.impl;

import com.example.demo.repository.*;
import com.example.demo.service.EmployeeService;
import com.example.demo.model.Visit;
import java.time.LocalDate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import  com.example.demo.model.*;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PatientRepository patientRepository;
    private final AdmissionRepository admissionRepository;
    private final VisitRepository visitRepository;
    private final DoctorRepository doctorRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository,PatientRepository patientRepository,AdmissionRepository admissionRepository,VisitRepository visitRepository,DoctorRepository doctorRepository) {

        this.employeeRepository = employeeRepository;
        this.patientRepository = patientRepository;
        this.admissionRepository = admissionRepository;
        this.visitRepository = visitRepository;
        this.doctorRepository = doctorRepository;

    }


    @Override
    public boolean registerPatient(String employeeID,String patientID){
        boolean isPatient = patientRepository.existsByPatientId(patientID);

        if (isPatient){
            VisitId id =  new VisitId();
            id.setPatientId(patientID);
            id.setRegistrationDate(LocalDate.now());

            Visit visit = new Visit();
            visit.setId(id);
            Patient p = patientRepository.findByPatientId(patientID).get();
            visit.setPatient(p);
            Employee e = employeeRepository.findByEmployeeId(employeeID).get();
            visit.setEmployee(e);

            Visit v = visitRepository.save(visit);
            return v != null;

        }
        return false;
    }
    @Override
    public boolean assignDocToPatient(String doctorID, String patientID){
        boolean isRegisteredDate = visitRepository.existsById_PatientIdAndId_RegistrationDate(patientID,LocalDate.now());

        if(isRegisteredDate){
            System.out.println("in");
                AdmissionId id = new AdmissionId();
                id.setPatientId(patientID);
                id.setAdmissionDate(LocalDate.now());

                Admission admission = new Admission();
                admission.setId(id);
                Patient p = patientRepository.findByPatientId(patientID).get();
                admission.setPatient(p);
                Doctor d = doctorRepository.findByDoctorId(doctorID).get();
                admission.setDoctor(d);

                Admission a = admissionRepository.save(admission);
                return a !=  null;

            }
            return false;
        }
        @Override
        public String getPatientsByRegisterDateJson(LocalDate date){
        List <Patient> Pats = visitRepository.findPatientsByVisitDate(date);
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                return objectMapper.writeValueAsString(Pats);
            } catch (Exception e) {
                System.out.println(e);
                return null;
            }
        }
        }
