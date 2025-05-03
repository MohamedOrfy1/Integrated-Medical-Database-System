package com.example.demo.service.impl;

import com.example.demo.service.PDFGenService;
import java.io.ByteArrayOutputStream;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.Map;
import java.nio.file.Paths;
import java.util.Random;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;


@Service
public class PDFGenServiceImpl implements PDFGenService {

    public PDFGenServiceImpl() {
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
    public String replacePlaceholders(){ // for testing purposes waiting for DTOs to be used
        Random random = new Random();
        String html = htmlFileToString();
        String singlerow;
        String tbody = "";

        for (int i = 0;i<7;i++){
            singlerow = "<tr>" +
                    "<td>" + String.valueOf(random.nextInt()) +"</td>" +
                    "<td>" + String.valueOf(random.nextInt()) +"</td>" +
                    "<td>" + String.valueOf(random.nextInt()) +"</td>" +
                    "<td class=\"reference-range\">" + String.valueOf(random.nextInt()) +"</td>" +
                    "<td>" + String.valueOf(random.nextInt()) +"</td>" +
                    "</tr>";
            tbody = tbody + singlerow;
        }
        Map<String, String> values = Map.of(
                "name", "Tamim Sherif",
                "age", "23",
                "gender", "male",
                "printdate", "23-12-2003",
                "sampledate", "24-12-2024",
                "dr", "Tamim",
                "testid", "24512",
                "comments", "zy el fol.",
                "tests" , tbody
        );
        for (Map.Entry<String, String> entry : values.entrySet()) {
            html = html.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }

        return html;


    }
    }






