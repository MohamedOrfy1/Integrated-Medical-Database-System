package com.example.demo.service.impl;
import com.example.demo.DTO.PatientResponseDTO;
import com.example.demo.model.Admission;
import com.example.demo.model.Patient;
import com.example.demo.repository.AdmissionRepository;
import com.example.demo.service.PatientSearchService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientSearchServiceImpl implements PatientSearchService {
    private final AdmissionRepository admissionRepository;


    public PatientSearchServiceImpl(AdmissionRepository admissionRepository) {
        this.admissionRepository = admissionRepository;

    }
    @Override
    public Optional<Patient> searchAdmittedPatients(String doctorId, String nationalId)
    {
        return admissionRepository.findPatientByDoctorAndNationalId(doctorId, nationalId);
    }
    public List<PatientResponseDTO> searchPatientsWithFilters(
            String doctorId,
            LocalDate startDate,
            LocalDate endDate,
            Integer minAge,
            Integer maxAge) {

        // First filter by date range if provided
        List<Admission> admissions = admissionRepository.findByDoctorIdAndDateRange(
                doctorId,
                startDate,
                endDate);

        // Then filter by age range in memory
        return admissions.stream()
                .map(admission -> {
                    Patient patient = admission.getPatient();
                    int age = calculateAge(patient);
                    return new PatientAdmission(admission, age);
                })
                .filter(pa -> minAge == null || pa.age() >= minAge)
                .filter(pa -> maxAge == null || pa.age() <= maxAge)
                .map(pa -> convertToResponseDTO(pa.admission().getPatient(), pa.admission().getId().getAdmissionDate()))
                .collect(Collectors.toList());
    }
    private record PatientAdmission(Admission admission, int age) {}
    private PatientResponseDTO convertToResponseDTO(Patient patient, LocalDate admissionDate) {
        PatientResponseDTO dto = new PatientResponseDTO();
        dto.setPatientId(patient.getPatientId());
        dto.setName(patient.getFirstName() + " " + patient.getFatherName() + " " +
                patient.getGrandfatherName() + " " + patient.getFamilyName());
        dto.setAdmissionDate(admissionDate);
        dto.setAge(calculateAge(patient));
        dto.setGender(getPatientGender(Integer.parseInt(patient.getPatientId().substring(12,13))));
        return dto;
    }
    @Override
    public LocalDate getPatientDOB(String yymmdd,int centuryDigit) {

        int year = Integer.parseInt(yymmdd.substring(0, 2));
        int month = Integer.parseInt(yymmdd.substring(2, 4));
        int day = Integer.parseInt(yymmdd.substring(4, 6));
        int fullYear = switch (centuryDigit) {
            case 2 -> 1900 + year;
            case 3 -> 2000 + year;
            case 4 -> 2100 + year;
            default -> throw new IllegalArgumentException("Invalid century digit");
        };
        return LocalDate.of(fullYear, month, day);
    }
    @Override
    public int calculateAge(Patient patient) {
        String yymmdd=patient.getPatientId().substring(1,7);
        int centuryDigit=Integer.parseInt(patient.getPatientId().substring(0,1));
        LocalDate dob=getPatientDOB(yymmdd,centuryDigit);
        return Period.between(dob, LocalDate.now()).getYears();
    }
    @Override
    public String getPatientGender(int digit) {
        if (digit%2==0)
        {
            return "female";
        }
        else {
            return "male";
        }
    }
}