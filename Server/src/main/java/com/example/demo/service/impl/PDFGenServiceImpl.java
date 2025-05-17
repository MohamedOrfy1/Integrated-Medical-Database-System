package com.example.demo.service.impl;

import com.example.demo.service.PDFGenService;
import java.io.ByteArrayOutputStream;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.Map;
import org.json.JSONArray;
import java.nio.file.Paths;
import java.util.Random;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;


@Service
public class PDFGenServiceImpl implements PDFGenService {

    public PDFGenServiceImpl() {
    }
    @Override
    public boolean convertXhtmlToPdf(String HtmlString) throws Exception{
        System.out.println("1");
        String FILE_DIRECTORY = "Server/src/main/java/com/example/demo/PDFDocs/" ;
        System.out.println("2");
        Path filePath = Paths.get(FILE_DIRECTORY).resolve("output.pdf").normalize();
        System.out.println("3");

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(HtmlString, null);
            builder.useFastMode();
            builder.toStream(os);
            builder.run();
            Files.write(filePath,os.toByteArray());
            System.out.println("4");
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("hhh");
            System.out.println("5");
            return false;
            }
        System.out.println("hhhhkk");
        return true;
    }
    @Override
    public String htmlFileToString(){
        System.out.println("1.6");
        try {
            System.out.println("1.7");
            byte[] bytes = Files.readAllBytes(Paths.get("Server/src/main/resources/templates/report.html"));
            System.out.println("1.5.1");
            return new String(bytes, StandardCharsets.UTF_8);
        }catch(Exception e)
        {
            
            System.out.println("sdfsdf");
        }
        System.out.println("1.5");
        return null;
    }

    @Override
    public String replacePlaceholders(String ReportJson){ // for testing purposes waiting for DTOs to be used
        System.out.println("1.1");
        JSONObject root = new JSONObject(ReportJson);
        System.out.println("1.2");

        JSONObject patientInfo = root.getJSONObject("patient_info");
        String name = patientInfo.getString("name");
        int age = patientInfo.getInt("age");
        String gender = patientInfo.getString("gender");
        String testId = patientInfo.getString("test_id");
        System.out.println("1.3");

        JSONObject testdetails  = root.getJSONObject("test_details");
        String sampledate = testdetails.getString("sample_date");
        System.out.println("1.44446");
        String printDate = testdetails.getString("print_date");
        System.out.println("1.44447");
        String dr = testdetails.getString("referring_physician");
        System.out.println("1.44448");

        JSONObject bloodReport = root.getJSONObject("blood_count_report");System.out.println("1.441654");
        JSONArray tests = bloodReport.getJSONArray("tests");System.out.println("1.45450004");
        String comments = bloodReport.getString("report_comments");

        System.out.println("1.44");
        String html = htmlFileToString();
        System.out.println("1.4");
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
    }






