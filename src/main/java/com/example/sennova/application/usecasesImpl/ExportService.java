package com.example.sennova.application.usecasesImpl;

import com.example.sennova.domain.model.EquipmentModel;
import com.example.sennova.domain.model.ReagentModel;
import com.example.sennova.domain.port.EquipmentPersistencePort;
import com.example.sennova.domain.port.ReagentPersistencePort;
import com.example.sennova.domain.port.TestRequestPersistencePort;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


// HERE THERE ARE ALL METHODS TO EXPORT DATA LIKE REAGENTS, EQUIPMENTS Y TEST REQUESTS


@Service
public class ExportService {

    private final EquipmentPersistencePort equipmentPersistencePort;
    private final ReagentPersistencePort reagentPersistencePort;




    @Autowired
    public ExportService(TestRequestPersistencePort testRequestPersistencePort,
                         EquipmentPersistencePort equipmentPersistencePort,
                         ReagentPersistencePort reagentPersistencePort, ReagentPersistencePort reagentPersistencePort1) {
        this.equipmentPersistencePort = equipmentPersistencePort;
        this.reagentPersistencePort = reagentPersistencePort1;

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




}