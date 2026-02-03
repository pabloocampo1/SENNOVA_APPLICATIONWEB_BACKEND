package com.example.sennova.application.usecasesImpl.export;

import com.example.sennova.application.dto.Export.ExportEquipmentPdfResponse;
import com.example.sennova.application.dto.Export.ExportReagentInfoRequest;
import com.example.sennova.domain.model.ReagentModel;
import com.example.sennova.domain.model.UserModel;
import com.example.sennova.domain.port.EquipmentPersistencePort;
import com.example.sennova.domain.port.ReagentPersistencePort;
import com.example.sennova.domain.port.TestRequestPersistencePort;

import com.example.sennova.domain.port.UserPersistencePort;
import com.example.sennova.infrastructure.persistence.entities.inventoryReagentsEntities.ReagentsEntity;
import com.example.sennova.infrastructure.persistence.entities.inventoryReagentsEntities.ReagentsUsageRecords;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


// HERE THERE ARE ALL METHODS TO EXPORT DATA LIKE REAGENTS, EQUIPMENTS Y TEST REQUESTS


@Service
public class ExportReagentService {


    private final ReagentPersistencePort reagentPersistencePort;
    private final UserPersistencePort userPersistencePort;

    @Autowired
    public ExportReagentService(TestRequestPersistencePort testRequestPersistencePort,
                                ReagentPersistencePort reagentPersistencePort, ReagentPersistencePort reagentPersistencePort1, UserPersistencePort userPersistencePort) {
        this.reagentPersistencePort = reagentPersistencePort1;

        this.userPersistencePort = userPersistencePort;
    }



    // method to generate the doc of reagents

    public byte[] generateReagentExcel() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Inventario Reactivos");


        String[] headers = {
                "Ubicación", "Nombre", "Marca", "Pureza",
                "Unidades", "Cantidad (g ó mL)", "Lote", "Fecha de vencimiento"
        };


        CellStyle instStyle = createHeaderBoxStyle(workbook);
        CellStyle tableHeaderStyle = createTableHeaderStyle(workbook);
        CellStyle bodyStyle = createBodyStyle(workbook);
        CellStyle dateStyle = createDateStyle(workbook, bodyStyle);


        for (int i = 0; i < 3; i++) {
            Row row = sheet.createRow(i);
            for (int j = 0; j < headers.length; j++) {
                row.createCell(j).setCellStyle(instStyle);
            }
        }


        sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 1));
        sheet.getRow(0).getCell(0).setCellValue("SENA \n SERVICIO NACIONAL DE APRENDIZAJE");


        sheet.addMergedRegion(new CellRangeAddress(0, 2, 2, 5));
        sheet.getRow(0).getCell(2).setCellValue(
                "Laboratorio de Servicios Tecnológicos\nSENA C.R.N.R La Salada\n\n" +
                        "Listado maestro de reactivos químicos"
        );


        sheet.addMergedRegion(new CellRangeAddress(0, 0, 6, 7));
        sheet.getRow(0).getCell(6).setCellValue("LABSYS ONE SOFTWARE.");
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 6, 7));
        sheet.getRow(1).getCell(6).setCellValue("Generado : " + LocalDate.now());
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 6, 7));
        sheet.getRow(2).getCell(6).setCellValue("");


        Row tableHeaderRow = sheet.createRow(4);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = tableHeaderRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(tableHeaderStyle);
        }


        List<ReagentModel> reagents = this.reagentPersistencePort.findAll();
        int dataRowNum = 5;

        for (ReagentModel r : reagents) {
            Row row = sheet.createRow(dataRowNum++);


            String locationName = (r.getLocation() != null) ? r.getLocation().getLocationName() : "N/A";
            createStyledCell(row, 0, locationName, bodyStyle);


            createStyledCell(row, 1, r.getReagentName(), bodyStyle);
            createStyledCell(row, 2, r.getBrand(), bodyStyle);
            createStyledCell(row, 3, r.getPurity(), bodyStyle);


            Cell unitCell = row.createCell(4);
            unitCell.setCellValue(r.getUnits() != null ? r.getUnits() : 0);
            unitCell.setCellStyle(bodyStyle);


            String qtyString = r.getQuantity() + " " + (r.getUnitOfMeasure() != null ? r.getUnitOfMeasure() : "");
            createStyledCell(row, 5, qtyString, bodyStyle);


            createStyledCell(row, 6, r.getBatch(), bodyStyle);


            if (r.getExpirationDate() != null) {
                Cell c = row.createCell(7);
                c.setCellValue(r.getExpirationDate());
                c.setCellStyle(dateStyle);
            } else {
                createStyledCell(row, 7, "N/A", bodyStyle);
            }
        }


        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return out.toByteArray();
    }

    private CellStyle createHeaderBoxStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        Font f = wb.createFont(); f.setBold(true); style.setFont(f);
        return style;
    }

    private CellStyle createTableHeaderStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.cloneStyleFrom(createHeaderBoxStyle(wb));
        style.setFillForegroundColor(IndexedColors.GREY_80_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font f = wb.createFont(); f.setBold(true); f.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(f);
        return style;
    }

    private CellStyle createBodyStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        return style;
    }

    private CellStyle createDateStyle(Workbook wb, CellStyle base) {
        CellStyle style = wb.createCellStyle();
        style.cloneStyleFrom(base);
        style.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat("yyyy-mm-dd"));
        return style;
    }

    private void createStyledCell(Row row, int col, String value, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(value == null ? "" : value);
        cell.setCellStyle(style);
    }


    public ExportEquipmentPdfResponse generateReagentInfoPdf(ExportReagentInfoRequest dto) throws Exception {
        ReagentsEntity reagent = this.reagentPersistencePort.findEntityById(dto.getReagentsId());
        UserModel userResponsibleDownload = this.userPersistencePort.findByEmail(dto.getResponsibleEmailDownload());


        String fileName = "Reactivo_" + reagent.getReagentName().replaceAll("\\s+", "_") + "_" + reagent.getBatch() + ".pdf";

        String html = buildReagentHtmlTemplate(reagent, userResponsibleDownload);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(baos);

            return new ExportEquipmentPdfResponse( fileName, baos.toByteArray());
        }
    }

    private String buildReagentHtmlTemplate(ReagentsEntity r, UserModel userResponsibleDownload) {
        // Manejo de estados para el color del badge de vencimiento
        String expirationColor = "#39A900"; // Verde SENA
        if ("VENCIDO".equalsIgnoreCase(r.getStateExpiration())) expirationColor = "#d32f2f"; // Rojo
        if ("PRÓXIMO A VENCER".equalsIgnoreCase(r.getStateExpiration())) expirationColor = "#f57c00"; // Naranja

        StringBuilder historyHtml = new StringBuilder();
        if (r.getReagentsUsageRecordsList() != null && !r.getReagentsUsageRecordsList().isEmpty()) {
            for (ReagentsUsageRecords record : r.getReagentsUsageRecordsList()) {
                historyHtml.append(String.format(
                        "<tr><td>%s</td><td>%s</td><td>%.2f %s</td><td>%s</td></tr>",
                        record.getCreateAt(),
                        escapeHtml(record.getUsedBy()),
                        record.getQuantity_used(),
                        escapeHtml(r.getUnitOfMeasure()),
                        escapeHtml(record.getNotes())
                ));
            }
        } else {
            historyHtml.append("<tr><td colspan='4' style='text-align:center;'>No hay registros de uso</td></tr>");
        }

        return """
    <!DOCTYPE html>
    <html>
    <head>
        <style>
            body { font-family: 'Helvetica', sans-serif; color: #333; margin: 0; padding: 20px; }
            .header { border-bottom: 3px solid #39A900; padding-bottom: 10px; margin-bottom: 20px; }
            .title { font-size: 22px; font-weight: bold; color: #39A900; }
            .section-title { 
                background-color: #f4f4f4; padding: 8px; font-weight: bold; 
                border-left: 5px solid #39A900; margin: 20px 0 10px 0; font-size: 14px;
            }
            table { width: 100%%; border-collapse: collapse; margin-bottom: 10px; }
            td, th { padding: 8px; border-bottom: 1px solid #eee; font-size: 11px; text-align: left; }
            .label { font-weight: bold; color: #555; width: 25%%; }
            .badge {
                padding: 3px 8px; border-radius: 4px; font-size: 10px;
                font-weight: bold; color: white; display: inline-block;
            }
            .history-table th { background-color: #f9f9f9; color: #39A900; border-bottom: 2px solid #39A900; }
        </style>
    </head>
    <body>
        <div class="header">
            <div class="title">FICHA TÉCNICA DE REACTIVO</div>
            <div style="font-size: 12px; color: #666;">Sennova - Control de Sustancias Químicas</div>
        </div>

        <div class="section-title">INFORMACIÓN GENERAL</div>
        <table>
            <tr>
                <td class="label">Nombre:</td><td>%s</td>
                <td class="label">Marca:</td><td>%s</td>
            </tr>
            <tr>
                <td class="label">Pureza:</td><td>%s</td>
                <td class="label">Lote (Batch):</td><td>%s</td>
            </tr>
            <tr>
                <td class="label">Placa/Tag:</td><td>%s</td>
                <td class="label">Ubicación:</td><td>%s</td>
            </tr>
        </table>

        <div class="section-title">INVENTARIO Y ESTADO</div>
        <table>
            <tr>
                <td class="label">Cantidad Actual:</td>
                <td style="font-size: 14px; font-weight: bold;">%.2f %s</td>
                <td class="label">Unidades:</td><td>%d</td>
            </tr>
            <tr>
                <td class="label">Vencimiento:</td>
                <td><strong>%s</strong></td>
                <td class="label">Estado Vencimiento:</td>
                <td><span class="badge" style="background-color: %s;">%s</span></td>
            </tr>
        </table>

        <div class="section-title">HISTORIAL DE CONSUMO (Últimos registros)</div>
        <table class="history-table">
            <thead>
                <tr>
                    <th>Fecha</th>
                    <th>Responsable</th>
                    <th>Cant. Usada</th>
                    <th>Notas</th>
                </tr>
            </thead>
            <tbody>
                %s
            </tbody>
        </table>

        <div style="margin-top: 50px; text-align: center; font-size: 9px; color: #aaa; border-top: 1px solid #eee; padding-top: 10px;">
            Este documento es una copia fiel de los registros digitales en el sistema Sennova.<br/>
            Descargado por: %s | Fecha: %s
        </div>
    </body>
    </html>
    """.formatted(
                escapeHtml(r.getReagentName()),
                escapeHtml(r.getBrand()),
                escapeHtml(r.getPurity()),
                escapeHtml(r.getBatch()),
                escapeHtml(r.getSenaInventoryTag()),
                r.getLocation() != null ? escapeHtml(r.getLocation().getLocationName()) : "N/A",
                r.getQuantity(),
                escapeHtml(r.getUnitOfMeasure()),
                r.getUnits(),
                r.getExpirationDate(),
                expirationColor,
                escapeHtml(r.getStateExpiration() != null ? r.getStateExpiration() : "N/A"),
                historyHtml.toString(),
                userResponsibleDownload.getName() != null ? escapeHtml(userResponsibleDownload.getName()) : "Sistema",
                java.time.LocalDateTime.now()
        );
    }

    private String escapeHtml(String input) {
        if (input == null || input.isBlank()) return "N/A";
        return input.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
    }

    





}