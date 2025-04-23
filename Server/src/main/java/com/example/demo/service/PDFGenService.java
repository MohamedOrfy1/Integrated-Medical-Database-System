package com.example.demo.service;




public interface PDFGenService {
    boolean convertXhtmlToPdf(String HtmlString) throws Exception;
    String htmlFileToString();
    String replacePlaceholders();

}