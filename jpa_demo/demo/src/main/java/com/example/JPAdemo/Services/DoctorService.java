package com.example.JPAdemo.Services;

import com.example.JPAdemo.models.Patient;
import com.example.JPAdemo.repositories.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    private final Admission_repo admissionRepository;
    private final PatientRepo patientRepository;
    private final Diagnosis_repo diagnosisRepository;
    private final MedicalImages_repo medicalImagesRepository;
    private final Blood_test_repo bloodTestRepository;

    public DoctorService(Admission_repo admissionRepository,
                         PatientRepo patientRepository,
                         Diagnosis_repo diagnosisRepository,
                         MedicalImages_repo medicalImagesRepository,
                         Blood_test_repo bloodTestRepository) {
        this.admissionRepository = admissionRepository;
        this.patientRepository = patientRepository;
        this.diagnosisRepository = diagnosisRepository;
        this.medicalImagesRepository = medicalImagesRepository;
        this.bloodTestRepository = bloodTestRepository;
    }
    public List<Patient> getAdmittedPatients(String doctorId) {
        return null;
    }
    public List<Patient> filterPatientsByDiagnosis(Long doctorId, String diagnosis) {
        return null;
    }
}
