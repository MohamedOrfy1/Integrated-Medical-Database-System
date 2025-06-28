package com.example.demo.service.impl;

import com.example.demo.DTO.BloodTestAttributeDTO;
import com.example.demo.DTO.TestHeaderInfoDTO;
import com.example.demo.service.PDFGenService;
import java.io.ByteArrayOutputStream;
import java.nio.file.Path;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import java.nio.file.Paths;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;


@Service
public class PDFGenServiceImpl implements PDFGenService {
    private final CommonServiceImpl commonService;

    public PDFGenServiceImpl(CommonServiceImpl commonService) {
        this.commonService = commonService;

    }
    @Override
    public boolean convertXhtmlToPdf(String HtmlString) throws Exception{

        String FILE_DIRECTORY = "src/main/java/com/example/demo/PDFDocs/" ;
        Path filePath = Paths.get(FILE_DIRECTORY).resolve("output.pdf").normalize();


        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(HtmlString, null);
            builder.useFastMode();
            builder.toStream(os);
            builder.run();
            Files.write(filePath,os.toByteArray());
        } catch (Exception e) {
            System.out.println(e);
            return false;
            }
        return true;
    }
    @Override
    public String htmlFileToString(){

        try {
            byte[] bytes = Files.readAllBytes(Paths.get("src/main/resources/templates/report.html"));
            return new String(bytes, StandardCharsets.UTF_8);
        }catch(Exception e)
        {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public String replacePlaceholders(String ReportJson){ // for testing purposes waiting for DTOs to be used

        JSONObject root = new JSONObject(ReportJson);

        JSONObject patientInfo = root.getJSONObject("patient_info");
        String name = patientInfo.getString("name");
        int age = patientInfo.getInt("age");
        String gender = patientInfo.getString("gender");
        String testId = patientInfo.getString("test_id");


        JSONObject testdetails  = root.getJSONObject("test_details");
        String sampledate = testdetails.getString("sample_date");
        String printDate = testdetails.getString("print_date");
        String dr = testdetails.getString("referring_physician");

        JSONObject bloodReport = root.getJSONObject("blood_count_report");
        JSONArray tests = bloodReport.getJSONArray("tests");
        String comments = bloodReport.getString("report_comments");

        String html = htmlFileToString();
        String singlerow;
        String tbody = "";

        for (int i = 0; i < tests.length(); i++) {
            JSONObject test = tests.getJSONObject(i);
            String testName = test.getString("test_name");
            double result = test.getDouble("result");
            String unit = test.getString("unit");
            String referenceRange = test.getString("reference_range");
            String flag = test.getString("flag");

            singlerow = "<tr>" +
                    "<td>" + testName +"</td>" +
                    "<td>" + String.valueOf(result) +"</td>" +
                    "<td>" + unit +"</td>" +
                    "<td class=\"reference-range\">" + referenceRange +"</td>" +
                    "<td>" + flag +"</td>" +
                    "</tr>";
            tbody = tbody + singlerow;
        }
        Map<String, String> values = Map.of(
                "name", name,
                "age", String.valueOf(age),
                "gender", gender,
                "printdate", printDate,
                "sampledate", sampledate,
                "dr", dr,
                "testid", testId,
                "comments", comments,
                "tests" , tbody
        );
        for (Map.Entry<String, String> entry : values.entrySet()) {
            html = html.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }

        return html;
    }
    @Override
    public String replacePlaceholdersFromDTO(TestHeaderInfoDTO hd, List<BloodTestAttributeDTO> atts){
        int age = commonService.getAgeFromID(hd.getPatientId());
        String gender = commonService.isMale(hd.getPatientId()) ? "Male" : "Female";
        String comments = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");


        String html = htmlFileToString();
        String singlerow;
        String tbody = "";

        for (BloodTestAttributeDTO att:atts) {
            String testName = att.getAttributeName();
            double result = att.getAttributeValue();
            String unit = att.getUnit();
            String referenceRange = String.valueOf(att.getFromRange()) +" - "+ String.valueOf(att.getToRange());
            String flag = att.isWithinRange() ? "Normal" : "Abnormal";

            singlerow = "<tr>" +
                    "<td>" + testName +"</td>" +
                    "<td>" + String.valueOf(result) +"</td>" +
                    "<td>" + unit +"</td>" +
                    "<td class=\"reference-range\">" + referenceRange +"</td>" +
                    "<td>" + flag +"</td>" +
                    "</tr>";
            tbody = tbody + singlerow;
        }
        Map<String, String> values = Map.of(
                "name", hd.getPatientName(),
                "age", String.valueOf(age),
                "gender", gender,
                "printdate", hd.getPrintingDate().format(formatter),
                "sampledate", hd.getSampleDate().format(formatter),
                "dr", hd.getReviewingDoctorName(),
                "assist", hd.getAssistingDoctorName(),
                "testid", String.valueOf(hd.getTestId()),
                "comments", hd.getComments(),
                "tests" , tbody
        );
        for (Map.Entry<String, String> entry : values.entrySet()) {
            html = html.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }

        return html;
    }
    }