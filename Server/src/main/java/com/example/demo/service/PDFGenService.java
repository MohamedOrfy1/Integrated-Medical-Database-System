package com.example.demo.service;


import com.example.demo.DTO.BloodTestAttributeDTO;
import com.example.demo.DTO.TestHeaderInfoDTO;

import java.util.List;

public interface PDFGenService {
    boolean convertXhtmlToPdf(String HtmlString) throws Exception;
    String htmlFileToString();
    String replacePlaceholders(String ReportJson);
    String replacePlaceholdersFromDTO(TestHeaderInfoDTO hd, List<BloodTestAttributeDTO> atts);

}