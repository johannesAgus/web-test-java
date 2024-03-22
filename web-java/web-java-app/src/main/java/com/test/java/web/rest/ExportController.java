package com.test.java.web.rest;

import com.lowagie.text.DocumentException;
import com.test.java.domain.Member;
import com.test.java.generator.ExcelGenerator;
import com.test.java.generator.PDFExporter;
import com.test.java.service.impl.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/export/")
public class ExportController {

    @Autowired
    private MemberService memberService;

    @GetMapping("excel")
    public void exportIntoExcelFile(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Member" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<Member> listOfStudents = memberService.findList();
        ExcelGenerator generator = new ExcelGenerator(listOfStudents);
        generator.generateExcelFile(response);
    }

    @GetMapping("pdf")
    public void exportToPDF(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Member" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<Member> memberList = memberService.findList();

        PDFExporter exporter = new PDFExporter(memberList);
        exporter.export(response);

    }

}
