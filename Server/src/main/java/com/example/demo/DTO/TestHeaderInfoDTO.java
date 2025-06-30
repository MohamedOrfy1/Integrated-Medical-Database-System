package com.example.demo.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class TestHeaderInfoDTO {
    private final Integer testId;
    private final String testType;
    private final LocalDate sampleDate;
    private final LocalDate printingDate;
    private final String patientId;
    private final String patientName;
    private final String assistingDoctorName;
    private final String reviewingDoctorName;
    private final String comments;

    public TestHeaderInfoDTO(
            Integer testId,
            String testType,
            LocalDate sampleDate,
            LocalDate printingDate,
            String patientId,
            String patientName,
            String assistingDoctorName,
            String reviewingDoctorName,
            String comments) {
        this.testId = testId;
        this.testType = testType;
        this.sampleDate = sampleDate;
        this.printingDate = printingDate;
        this.patientId = patientId;
        this.patientName = patientName;
        this.assistingDoctorName = assistingDoctorName;
        this.reviewingDoctorName = reviewingDoctorName;
        this.comments = comments;
    }
    @Override
    public String toString() {
        return String.format("Test ID: %d | Type: %s | Sample Date: %s | Patient: %s (%s)%n" +
                        "Assisting Doctor: %s | Reviewing Doctor: %s%n" +
                        "Printing Date: %s",
                testId,
                testType != null ? testType : "N/A",
                sampleDate != null ? sampleDate.toString() : "N/A",
                patientName != null ? patientName : "Unknown",
                patientId != null ? patientId : "N/A",
                assistingDoctorName != null ? assistingDoctorName : "Unassigned",
                reviewingDoctorName != null ? reviewingDoctorName : "Unassigned ",
                comments,
                printingDate != null ? printingDate.toString() : "null");
    }
}
