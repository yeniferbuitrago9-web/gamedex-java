package com.proyecto.gamedex.service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Font;
// ¡CAMBIO CLAVE! OpenPDF/Lowagie usa la clase Color de Java AWT para los colores
import java.awt.Color; 
import com.lowagie.text.pdf.PdfPCell; 
import com.lowagie.text.Rectangle; 
// [NUEVA IMPORTACIÓN REQUERIDA]
import com.lowagie.text.Phrase; 
// [FIN DE NUEVA IMPORTACIÓN]

import com.proyecto.gamedex.model.Usuario;

import org.apache.poi.ss.usermodel.Row; 
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
    //  PDF
    // ---------------------------
    public void exportarUsuariosPdf(List<Usuario> usuarios, OutputStream outputStream)
            throws DocumentException, IOException {

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, outputStream);
        document.open();

        // 1. DEFINICIÓN DE FUENTES Y COLORES
        Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
        Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD, Color.BLACK); // Fuente para encabezados
        Color lightGray = new Color(200, 200, 200); // Gris para el título (más oscuro)
        Color mediumGray = new Color(220, 220, 220); // Gris para encabezados (un poco más claro)
        
        // 2. LÓGICA DEL TÍTULO (con fondo gris)
        Paragraph title = new Paragraph("Reporte de Usuarios", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        
        PdfPTable titleTable = new PdfPTable(1);
        titleTable.setWidthPercentage(100);
        titleTable.setSpacingAfter(10f); // Espacio después del título
        
        Phrase titlePhrase = new Phrase(title); // Usamos el párrafo como Phrase para evitar errores
        
        PdfPCell titleCell = new PdfPCell(titlePhrase);
        titleCell.setBackgroundColor(lightGray); // Aplica el fondo gris del título
        titleCell.setBorder(Rectangle.NO_BORDER); 
        titleCell.setPadding(8); 
        titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        
        titleTable.addCell(titleCell);
        document.add(titleTable); 
        
        // 3. CONFIGURACIÓN E INYECCIÓN DE ENCABEZADOS DE LA TABLA (con fondo gris)
        PdfPTable table = new PdfPTable(6); 
        table.setWidthPercentage(100);
        
        String[] headers = {"ID", "Nombre", "Email", "Rol", "Documento", "Teléfono"};
        PdfPCell cell;
        
        // ITERAMOS Y AGREGAMOS CADA ENCABEZADO CON FONDO GRIS
        for (String headerText : headers) {
            cell = new PdfPCell(new Phrase(headerText, headerFont));
            cell.setBackgroundColor(mediumGray); // <-- APLICA EL FONDO GRIS A LOS ENCABEZADOS
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);
        }
        
        // --- Llenar la tabla con los datos ---

        for (Usuario u : usuarios) {
            // 1. ID
            table.addCell(String.valueOf(u.getIdUsuario())); 
            
            // 2. Nombre
            table.addCell(u.getNombre()); 
            
            // 3. Email
            table.addCell(u.getEmail()); 
            
            // 4. Rol (Muestra el nombre del primer rol encontrado o "Sin Rol" si no tiene)
            String rolDisplay = u.getRoles().isEmpty() ? "Sin Rol" : u.getRoles().iterator().next().getNombre();
            table.addCell(rolDisplay); 
            
            // 5. Documento
            table.addCell(u.getDocUsuario()); 
            
            // 6. Teléfono
            // Si el teléfono es null, ponemos "N/A"
            table.addCell(u.getTelefono() != null ? u.getTelefono() : "N/A"); 
        }

        // Agregar la tabla al documento
        document.add(table);
        document.close();
    }
    // ---------------------------
    //  EXCEL
    // ---------------------------
    public Workbook exportarUsuariosExcel(List<Usuario> usuarios) {

    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("Usuarios");

    // ENCABEZADOS
    Row header = sheet.createRow(0);
    header.createCell(0).setCellValue("ID");
    header.createCell(1).setCellValue("Documento");
    header.createCell(2).setCellValue("Nombre");
    header.createCell(3).setCellValue("Email");
    header.createCell(4).setCellValue("Teléfono");
    header.createCell(5).setCellValue("Creado");
    header.createCell(6).setCellValue("Actualizado");
    header.createCell(7).setCellValue("Roles");

    int rowNumber = 1;

    // FILAS
    for (Usuario u : usuarios) {
        Row row = sheet.createRow(rowNumber++);

        row.createCell(0).setCellValue(u.getIdUsuario());
        row.createCell(1).setCellValue(u.getDocUsuario());
        row.createCell(2).setCellValue(u.getNombre());
        row.createCell(3).setCellValue(u.getEmail());
        row.createCell(4).setCellValue(u.getTelefono() != null ? u.getTelefono() : "");
        row.createCell(5).setCellValue(u.getCreatedAt() != null ? u.getCreatedAt().toString() : "");
        row.createCell(6).setCellValue(u.getUpdatedAt() != null ? u.getUpdatedAt().toString() : "");

        // Convertir roles a una lista separada por comas
        String roles = u.getRoles().stream()
                .map(r -> r.getNombre()) 
                .reduce((a, b) -> a + ", " + b)
                .orElse("");

        row.createCell(7).setCellValue(roles);
    }

    // Ajuste automático del ancho de columna
    for (int i = 0; i < 8; i++) {
        sheet.autoSizeColumn(i);
    }

    return workbook;
}
}