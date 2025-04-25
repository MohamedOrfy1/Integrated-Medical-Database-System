package com.example.demo.service.impl;

import com.example.demo.repository.PatientRepository;
import com.example.demo.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.Patient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Boolean InsertJsonPatient (String JsonPatient) {

        Patient p = new Patient();
        int affected_rows = 0;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            p = objectMapper.readValue(JsonPatient, Patient.class);
        }catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid patient JSON: " + e.getOriginalMessage());
        }
        if (!isPatientExist(p.getPatientId())&&isPatientValid(p))
        { affected_rows = patientRepository.insertPatient(p);
        }
        return affected_rows > 0;
    }

    @Override
    public String getPatientsInJson() {
        List<Patient> patients = patientRepository.findAll();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(patients);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert patients to JSON", e);
        }
    }

    @Override
    public Boolean isPatientExist(String id) {
        if(patientRepository.findByPatientId(id).isEmpty()) return false;
        else return true;
    }

    @Override
    public Boolean isPatientValid(Patient patient){
        if(patient.getFirstName().trim().equals("")||patient.getFatherName().trim().equals("")||patient.getGrandfatherName().trim().equals(""))
        {
            return false;
        }
        return isValidNationalId(patient.getPatientId())&&isValidPhoneNumber(patient.getPhoneNumber());
    }

    @Override
    public Boolean isValidBirthDate(String yymmdd, int centuryDigit) {
        try {
            int year = Integer.parseInt(yymmdd.substring(0, 2));
            int month = Integer.parseInt(yymmdd.substring(2, 4));
            int day = Integer.parseInt(yymmdd.substring(4, 6));
            int fullYear = switch (centuryDigit) {
                case 2 -> 1900 + year;
                case 3 -> 2000 + year;
                case 4 -> 2100 + year;
                default -> throw new IllegalArgumentException("Invalid century digit");
            };
            LocalDate birthDate = LocalDate.of(fullYear, month, day);
            LocalDate today = LocalDate.now();
            return !birthDate.isAfter(today);
        } catch (DateTimeException | IllegalArgumentException e) {
            return false;
        }
    }
@Override
    public Boolean isValidNationalId(String nationalId) {
    try{
        int centuryDigit = Character.getNumericValue(nationalId.charAt(0));
        String yymmdd = nationalId.substring(1, 7);
        int governorateCode = Integer.parseInt(nationalId.substring(7, 9));
        if (nationalId.length() != 14||nationalId.trim().equals("")) {
            return false;
        }
        for (char c : nationalId.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        if (centuryDigit < 2 || centuryDigit > 3) {
            return false;
        }
        if (governorateCode < 1 || governorateCode > 35) {
            return false;
        }
        for (char c : nationalId.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return isValidBirthDate(yymmdd,centuryDigit);
    }
    catch(StringIndexOutOfBoundsException s)
    {return false;}
    }
   @Override
    public Boolean isValidPhoneNumber(String phoneNumber){
        if (phoneNumber.length() != 11) {
            return false;
        }
        if (!(phoneNumber.startsWith("010") ||
                phoneNumber.startsWith("011") ||
                phoneNumber.startsWith("012") ||
                phoneNumber.startsWith("015"))) {
            return false;
        }
        return phoneNumber.chars().allMatch(Character::isDigit);
    }


}