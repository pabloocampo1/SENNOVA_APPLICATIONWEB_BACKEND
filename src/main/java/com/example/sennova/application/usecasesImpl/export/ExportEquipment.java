package com.example.sennova.application.usecasesImpl.export;

import com.example.sennova.application.dto.Export.ExportEquipmentPdfResponse;
import com.example.sennova.domain.model.EquipmentModel;
import com.example.sennova.domain.port.EquipmentPersistencePort;

import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.EquipmentEntity;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
public class ExportEquipment {

    private final EquipmentPersistencePort equipmentPersistencePort;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public ExportEquipment(EquipmentPersistencePort equipmentPersistencePort) {
        this.equipmentPersistencePort = equipmentPersistencePort;
    }


    public byte[] generateEquipmentExcel() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Inventario Sennova");


        String[] headers = {
                "Código interno", "Nombre del equipo", "Marca", "Modelo",
                "Serial", "Placa SENA", "Estado", "Ubicación",
                "Amperaje (A)", "Voltaje (V)", "Costo", "Responsable",
                "Fecha Adquisición", "Mantenimiento", "Descripción del Equipo"
        };


        CellStyle instStyle = workbook.createCellStyle();
        instStyle.setBorderTop(BorderStyle.THIN);
        instStyle.setBorderBottom(BorderStyle.THIN);
        instStyle.setBorderLeft(BorderStyle.THIN);
        instStyle.setBorderRight(BorderStyle.THIN);
        instStyle.setAlignment(HorizontalAlignment.CENTER);
        instStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        instStyle.setWrapText(true);
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        instStyle.setFont(boldFont);


        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.cloneStyleFrom(instStyle);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_80_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font whiteFont = workbook.createFont();
        whiteFont.setBold(true);
        whiteFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(whiteFont);


        CellStyle bodyStyle = workbook.createCellStyle();
        bodyStyle.setBorderBottom(BorderStyle.THIN);
        bodyStyle.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());


        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.cloneStyleFrom(bodyStyle);
        dateStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("yyyy-mm-dd"));

        CellStyle currencyStyle = workbook.createCellStyle();
        currencyStyle.cloneStyleFrom(bodyStyle);
        currencyStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("$#,##0.00"));


        for (int i = 0; i < 3; i++) {
            Row row = sheet.createRow(i);
            for (int j = 0; j < headers.length; j++) {
                row.createCell(j).setCellStyle(instStyle);
            }
        }


        sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 1));
        sheet.getRow(0).getCell(0).setCellValue("SENA \n SERVICIO NACIONAL DE APRENDIZAJE");


        sheet.addMergedRegion(new CellRangeAddress(0, 2, 2, 11));
        sheet.getRow(0).getCell(2).setCellValue(
                "Laboratorio de Servicios Tecnológicos\nSENA C.R.N.R La Salada\n\n" +
                        "Listado maestro de equipos e instrumentos de laboratorio"
        );


        sheet.addMergedRegion(new CellRangeAddress(0, 0, 12, 14));
        sheet.getRow(0).getCell(12).setCellValue("LABSYS ONE SOFTWARE. ");
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 12, 14));
        sheet.getRow(1).getCell(12).setCellValue("Fecha de descarga inventario: " + LocalDate.now());
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 12, 14));
        sheet.getRow(2).getCell(12).setCellValue(" ");


        int headerRowNum = 4;
        Row tableHeaderRow = sheet.createRow(headerRowNum);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = tableHeaderRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }


        List<EquipmentModel> models = this.equipmentPersistencePort.findAll();
        int dataRowNum = 5;

        for (EquipmentModel eq : models) {
            Row row = sheet.createRow(dataRowNum++);

            createStyledCell(row, 0, eq.getInternalCode(), bodyStyle);
            createStyledCell(row, 1, eq.getEquipmentName(), bodyStyle);
            createStyledCell(row, 2, eq.getBrand(), bodyStyle);
            createStyledCell(row, 3, eq.getModel(), bodyStyle);
            createStyledCell(row, 4, eq.getSerialNumber(), bodyStyle);
            createStyledCell(row, 5, eq.getSenaInventoryTag(), bodyStyle);
            createStyledCell(row, 6, eq.getState(), bodyStyle);

            String loc = (eq.getLocation() != null) ? eq.getLocation().getLocationName() : "N/A";
            createStyledCell(row, 7, loc, bodyStyle);


            try {
                if (eq.getAmperage() != null) row.createCell(8).setCellValue(Double.parseDouble(eq.getAmperage()));
                if (eq.getVoltage() != null) row.createCell(9).setCellValue(Double.parseDouble(eq.getVoltage()));
            } catch (Exception e) {
                createStyledCell(row, 8, eq.getAmperage(), bodyStyle);
                createStyledCell(row, 9, eq.getVoltage(), bodyStyle);
            }


            Cell costCell = row.createCell(10);
            costCell.setCellValue(eq.getEquipmentCost());
            costCell.setCellStyle(currencyStyle);

            String resp = (eq.getResponsible() != null) ? eq.getResponsible().getName() : "Sin asignar";
            createStyledCell(row, 11, resp, bodyStyle);


            if (eq.getAcquisitionDate() != null) {
                Cell c = row.createCell(12);
                c.setCellValue(eq.getAcquisitionDate());
                c.setCellStyle(dateStyle);
            }
            if (eq.getMaintenanceDate() != null) {
                Cell c = row.createCell(13);
                c.setCellValue(eq.getMaintenanceDate());
                c.setCellStyle(dateStyle);
            }


            createStyledCell(row, 14, eq.getDescription(), bodyStyle);
        }


        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return out.toByteArray();
    }

    private void createStyledCell(Row row, int col, String value, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(value == null ? "" : value);
        cell.setCellStyle(style);
    }


    public ExportEquipmentPdfResponse exportEquipmentInfoPdf(Long equipmentId) throws Exception {
        EquipmentEntity equipment = this.equipmentPersistencePort.findEntityById(equipmentId);

        String fileName = "Equipo_" + equipment.getInternalCode().replaceAll("\\s+", "_") + ".pdf";

        // 2. Generar el HTML y el PDF
        String html = buildHtmlTemplate(equipment);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(baos);
            return new ExportEquipmentPdfResponse( fileName, baos.toByteArray());
        }
    }

    private String buildHtmlTemplate(EquipmentEntity eq) {
        // Manejo de fechas nulas para evitar errores en .formatted()
        String acquisition = (eq.getAcquisitionDate() != null) ? eq.getAcquisitionDate().toString() : "No registrada";
        String maintenance = (eq.getMaintenanceDate() != null) ? eq.getMaintenanceDate().toString() : "No programada";
        String description = (eq.getDescription() != null && !eq.getDescription().isBlank()) ? eq.getDescription() : "Sin descripción adicional.";

        return """
        <!DOCTYPE html>
        <html>
        <head>
            <style>
                body { font-family: 'Helvetica', sans-serif; color: #333; margin: 0; padding: 20px; }
                .header { border-bottom: 3px solid #39A900; padding-bottom: 10px; margin-bottom: 20px; }
                .title { font-size: 24px; font-weight: bold; color: #39A900; }
                .subtitle { font-size: 14px; color: #666; }
                
                .section-title { 
                    background-color: #f4f4f4; 
                    padding: 8px; 
                    font-weight: bold; 
                    border-left: 5px solid #39A900;
                    margin: 20px 0 10px 0;
                }
                
                table { width: 100%%; border-collapse: collapse; }
                td { padding: 8px; border-bottom: 1px solid #eee; font-size: 12px; vertical-align: top; }
                .label { font-weight: bold; color: #555; width: 25%%; }
                .value { color: #000; width: 25%%; }
                
                .badge {
                    padding: 4px 8px;
                    border-radius: 4px;
                    font-size: 10px;
                    font-weight: bold;
                    color: white;
                    display: inline-block;
                }
                .description-box { 
                    font-size: 12px; 
                    line-height: 1.5; 
                    color: #444; 
                    background: #fafafa; 
                    padding: 15px; 
                    border: 1px solid #eee;
                    min-height: 100px;
                }
                .footer { 
                    margin-top: 50px; 
                    text-align: center; 
                    font-size: 10px; 
                    color: #aaa; 
                    border-top: 1px solid #eee;
                    padding-top: 10px;
                }
            </style>
        </head>
        <body>
            <div class="header">
                <div class="title">HOJA DE VIDA DE EQUIPO</div>
                <div class="subtitle">Sennova - Sistema de Gestión de Laboratorio</div>
            </div>

            <div class="section-title">DATOS DE IDENTIFICACIÓN</div>
            <table>
                <tr>
                    <td class="label">Nombre del Equipo:</td><td class="value">%s</td>
                    <td class="label">Código Interno:</td><td class="value">%s</td>
                </tr>
                <tr>
                    <td class="label">Marca:</td><td class="value">%s</td>
                    <td class="label">Modelo:</td><td class="value">%s</td>
                </tr>
                 <tr>
                    <td class="label">Serial:</td><td class="value">%s</td>
                    <td class="label">Placa Inventario:</td><td class="value">%s</td>
                </tr>
                <tr>
                    <td class="label">Estado Actual:</td>
                    <td class="value"><span class="badge" style="background-color: #39A900;">%s</span></td>
                    <td class="label">Disponibilidad:</td>
                    <td class="value">%s</td>
                </tr>
            </table>

            <div class="section-title">ESPECIFICACIONES TÉCNICAS</div>
            <table>
                <tr>
                    <td class="label">Voltaje:</td><td class="value">%s</td>
                    <td class="label">Amperaje:</td><td class="value">%s</td>
                </tr>
                <tr>
                    <td class="label">Ubicación:</td><td class="value">%s</td>
                    <td class="label">Responsable:</td><td class="value">%s</td>
                </tr>
                <tr>
                    <td class="label">Fecha Adquisición:</td><td class="value">%s</td>
                    <td class="label">Mantenimiento:</td><td class="value">%s</td>
                </tr>
            </table>

            <div class="section-title">DESCRIPCIÓN Y OBSERVACIONES</div>
            <div class="description-box">
                %s
            </div>

            <div class="footer">
                Documento oficial generado por el sistema SENNOVA - Fecha de emisión: %s
            </div>
        </body>
        </html>
        """.formatted(
                escapeHtml(eq.getEquipmentName()),
                escapeHtml(eq.getInternalCode()),
                escapeHtml(eq.getBrand()),
                escapeHtml(eq.getModel()),
                escapeHtml(eq.getSerialNumber()),
                escapeHtml(eq.getSenaInventoryTag()),
                escapeHtml(eq.getState() != null ? eq.getState() : "N/A"),
                (eq.getAvailable() != null && eq.getAvailable()) ? "DISPONIBLE" : "NO DISPONIBLE",
                escapeHtml(eq.getVoltage()),
                escapeHtml(eq.getAmperage()),
                eq.getLocation() != null ? escapeHtml(eq.getLocation().getLocationName()) : "Sin ubicación",
                eq.getResponsible() != null ? escapeHtml(eq.getResponsible().getName()) : "Sin responsable",
                acquisition,
                maintenance,
                escapeHtml(description),
                java.time.LocalDateTime.now().format(formatter)
        );
    }

    private String escapeHtml(String input) {
        if (input == null || input.isBlank()) return "N/A";
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }


    
}
