package com.test.java.generator;

import com.test.java.base.utils.ImageUtils;
import com.test.java.domain.Member;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.apache.poi.ss.util.CellUtil.createCell;

public class ExcelGenerator {

    private List<Member> memberList;

    private XSSFWorkbook workbook;

    private XSSFSheet sheet;

    public ExcelGenerator(List <Member> memberList) {
        this.memberList = memberList;
        workbook = new XSSFWorkbook();
    }

    private void writeHeader() {
        sheet = workbook.createSheet("Member");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "ID", style);
        createCell(row, 1, "ID KTP", style);
        createCell(row, 2, "Name", style);
        createCell(row, 3, "Email", style);
        createCell(row, 4, "Password", style);
        createCell(row, 5, "Phone Number", style);
        createCell(row, 6, "Date Of Birth", style);
        createCell(row, 7, "Gender", style);
        createCell(row, 8, "Delete Flag", style);
        createCell(row, 9, "Created At", style);
        createCell(row, 10, "Update At", style);
    }

    private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (valueOfCell instanceof Integer) {
            cell.setCellValue((Integer) valueOfCell);
        } else if (valueOfCell instanceof Long) {
            cell.setCellValue((Long) valueOfCell);
        } else if (valueOfCell instanceof String) {
            cell.setCellValue((String) valueOfCell);
        } else {
            cell.setCellValue((Boolean) valueOfCell);
        }
        cell.setCellStyle(style);
    }

    private void write() {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        for (Member member: memberList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, member.getId(), style);
            createCell(row, columnCount++, member.getIdKTP(), style);
            createCell(row, columnCount++, member.getName(), style);
            createCell(row, columnCount++, member.getEmail(), style);
            createCell(row, columnCount++, member.getPassword(), style);
            createCell(row, columnCount++, member.getPhoneNumber(), style);
            createCell(row, columnCount++, member.getDateOfBirth(), style);
            createCell(row, columnCount++, member.getGender(), style);
            createCell(row, columnCount++, member.isDeleteFlag(), style);
        }
    }

    public void generateExcelFile(HttpServletResponse response) throws IOException {
        writeHeader();
        write();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

}
