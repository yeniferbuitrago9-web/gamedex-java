package com.proyecto.gamedex.service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Font; // 👈 FORZAMOS ESTA FONT (iText)
import com.proyecto.gamedex.model.Usuario;

import org.apache.poi.ss.usermodel.Row;  // 👈 FORZAMOS ESTE Row (POI)
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Service
public class ReporteService {

    // ---------------------------
    //  PDF
    // ---------------------------
    public void exportarUsuariosPdf(List<Usuario> usuarios, OutputStream outputStream)
            throws DocumentException, IOException {

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, outputStream);
        document.open();

        // Título
        Font font = new Font(Font.HELVETICA, 18, Font.BOLD);
        Paragraph title = new Paragraph("Reporte de Usuarios", font);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph(" ")); // espacio

        // Tabla
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);

        table.addCell("ID");
        table.addCell("Nombre");
        table.addCell("Email"); // campo correcto

        for (Usuario u : usuarios) {
            table.addCell(String.valueOf(u.getIdUsuario()));
            table.addCell(u.getNombre());
            table.addCell(u.getEmail());  // ✔ CAMBIADO
        }

        document.add(table);
        document.close();
    }

    // ---------------------------
    //  EXCEL
    // ---------------------------
    public Workbook exportarUsuariosExcel(List<Usuario> usuarios) {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Usuarios");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Nombre");
        header.createCell(2).setCellValue("Email"); // campo correcto

        int rowNumber = 1;

        for (Usuario u : usuarios) {
            Row row = sheet.createRow(rowNumber++);
            row.createCell(0).setCellValue(u.getIdUsuario());
            row.createCell(1).setCellValue(u.getNombre());
            row.createCell(2).setCellValue(u.getEmail()); // ✔ CAMBIADO
        }

        return workbook;
    }
}
