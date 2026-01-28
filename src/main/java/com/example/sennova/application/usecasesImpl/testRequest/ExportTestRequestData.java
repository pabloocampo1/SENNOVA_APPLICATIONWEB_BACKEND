package com.example.sennova.application.usecasesImpl.testRequest;
import com.example.sennova.domain.port.TestRequestPersistencePort;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.ProductEntity;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.SampleAnalysisEntity;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.SampleEntity;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.TestRequestEntity;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


@Service
public class ExportTestRequestData {

    private final TestRequestPersistencePort testRequestPersistencePort;

    public ExportTestRequestData(TestRequestPersistencePort port) {
        this.testRequestPersistencePort = port;
    }


    public byte[] generateAnnualAuditReport(String year) throws IOException {
        Workbook workbook = new XSSFWorkbook();


        List<TestRequestEntity> testRequests = testRequestPersistencePort.findAllByYear(year);


        createDashboardSheet(workbook, year, testRequests);
        createDetailedDataSheet(workbook, year, testRequests);


        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return out.toByteArray();
    }

    // ============================================================================
    // page 1: DASHBOARD
    // ============================================================================

    private void createDashboardSheet(Workbook workbook, String year,
                                      List<TestRequestEntity> testRequests) {
        Sheet sheet = workbook.createSheet("📊 Dashboard " + year);


        DashboardStyles styles = createDashboardStyles(workbook);

        int currentRow = 0;


        currentRow = buildMainHeader(sheet, year, styles, currentRow);
        currentRow += 2;


        currentRow = buildKPISection(sheet, testRequests, styles, currentRow);
        currentRow += 2;


        currentRow = buildStatusSummary(sheet, testRequests, styles, currentRow);
        currentRow += 2;


        currentRow = buildComplianceSummary(sheet, testRequests, styles, currentRow);


        for (int i = 0; i < 8; i++) {
            sheet.setColumnWidth(i, 18 * 256);
        }
    }

    private int buildMainHeader(Sheet sheet, String year, DashboardStyles styles, int startRow) {

        Row titleRow = sheet.createRow(startRow);
        titleRow.setHeightInPoints(35);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("REPORTE DE AUDITORÍA ANUAL - VIGENCIA " + year);
        titleCell.setCellStyle(styles.mainTitle);
        sheet.addMergedRegion(new CellRangeAddress(startRow, startRow, 0, 7));


        startRow++;
        Row subtitleRow = sheet.createRow(startRow);
        subtitleRow.setHeightInPoints(25);
        Cell subtitleCell = subtitleRow.createCell(0);
        subtitleCell.setCellValue("Laboratorio de Servicios Tecnológicos - SENA C.R.N.R La Salada");
        subtitleCell.setCellStyle(styles.subtitle);
        sheet.addMergedRegion(new CellRangeAddress(startRow, startRow, 0, 7));

        return startRow + 1;
    }

    private int buildKPISection(Sheet sheet, List<TestRequestEntity> testRequests,
                                DashboardStyles styles, int startRow) {

        Row sectionTitle = sheet.createRow(startRow++);
        Cell cell = sectionTitle.createCell(0);
        cell.setCellValue("📈 MÉTRICAS CLAVE");
        cell.setCellStyle(styles.sectionTitle);
        sheet.addMergedRegion(new CellRangeAddress(startRow - 1, startRow - 1, 0, 7));

        startRow++;


        int totalRequests = testRequests.size();
        int totalSamples = testRequests.stream()
                .mapToInt(r -> r.getSampleEntityList().size())
                .sum();
        int totalAnalysis = testRequests.stream()
                .flatMap(r -> r.getSampleEntityList().stream())
                .mapToInt(s -> s.getAnalysisEntities() != null ? s.getAnalysisEntities().size() : 0)
                .sum();


        startRow = createKPICard(sheet, startRow, 0, "Total Solicitudes",
                String.valueOf(totalRequests), styles);
        createKPICard(sheet, startRow - 2, 2, "Total Muestras",
                String.valueOf(totalSamples), styles);
        createKPICard(sheet, startRow - 2, 4, "Total Análisis",
                String.valueOf(totalAnalysis), styles);
        createKPICard(sheet, startRow - 2, 6, "Promedio Análisis/Solicitud",
                String.format("%.1f", (double) totalAnalysis / totalRequests), styles);

        return startRow;
    }

    private int createKPICard(Sheet sheet, int row, int col, String label,
                              String value, DashboardStyles styles) {

        Row labelRow = sheet.getRow(row);
        if (labelRow == null) labelRow = sheet.createRow(row);
        Cell labelCell = labelRow.createCell(col);
        labelCell.setCellValue(label);
        labelCell.setCellStyle(styles.kpiLabel);
        sheet.addMergedRegion(new CellRangeAddress(row, row, col, col + 1));


        Row valueRow = sheet.getRow(row + 1);
        if (valueRow == null) valueRow = sheet.createRow(row + 1);
        Cell valueCell = valueRow.createCell(col);
        valueCell.setCellValue(value);
        valueCell.setCellStyle(styles.kpiValue);
        sheet.addMergedRegion(new CellRangeAddress(row + 1, row + 1, col, col + 1));

        return row + 2;
    }

    private int buildStatusSummary(Sheet sheet, List<TestRequestEntity> testRequests,
                                   DashboardStyles styles, int startRow) {

        Row sectionTitle = sheet.createRow(startRow++);
        Cell cell = sectionTitle.createCell(0);
        cell.setCellValue("📋 RESUMEN POR ESTADO");
        cell.setCellStyle(styles.sectionTitle);
        sheet.addMergedRegion(new CellRangeAddress(startRow - 1, startRow - 1, 0, 7));

        startRow++;


        Row headerRow = sheet.createRow(startRow++);
        String[] headers = {"Estado", "Cantidad", "Porcentaje"};
        for (int i = 0; i < headers.length; i++) {
            Cell headerCell = headerRow.createCell(i);
            headerCell.setCellValue(headers[i]);
            headerCell.setCellStyle(styles.tableHeader);
        }


        java.util.Map<String, Long> statusCount = testRequests.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        TestRequestEntity::getState,
                        java.util.stream.Collectors.counting()
                ));


        for (java.util.Map.Entry<String, Long> entry : statusCount.entrySet()) {
            Row dataRow = sheet.createRow(startRow++);

            Cell stateCell = dataRow.createCell(0);
            stateCell.setCellValue(entry.getKey());
            stateCell.setCellStyle(styles.tableData);

            Cell countCell = dataRow.createCell(1);
            countCell.setCellValue(entry.getValue());
            countCell.setCellStyle(styles.tableData);

            Cell percentCell = dataRow.createCell(2);
            double percentage = (entry.getValue() * 100.0) / testRequests.size();
            percentCell.setCellValue(String.format("%.1f%%", percentage));
            percentCell.setCellStyle(styles.tableData);
        }

        return startRow;
    }

    private int buildComplianceSummary(Sheet sheet, List<TestRequestEntity> testRequests,
                                       DashboardStyles styles, int startRow) {

        Row sectionTitle = sheet.createRow(startRow++);
        Cell cell = sectionTitle.createCell(0);
        cell.setCellValue("✅ ANÁLISIS DE CUMPLIMIENTO");
        cell.setCellStyle(styles.sectionTitle);
        sheet.addMergedRegion(new CellRangeAddress(startRow - 1, startRow - 1, 0, 7));

        startRow++;


        int totalAnalysis = 0;
        int compliant = 0;
        int nonCompliant = 0;

        for (TestRequestEntity request : testRequests) {
            for (SampleEntity sample : request.getSampleEntityList()) {
                if (sample.getAnalysisEntities() != null) {
                    for (SampleAnalysisEntity analysis : sample.getAnalysisEntities()) {
                        totalAnalysis++;
                        String status = analysis.getPassStatus();
                        if ("NO CUMPLE".equalsIgnoreCase(status)) {
                            nonCompliant++;
                        } else if (status != null && !status.isEmpty()) {
                            compliant++;
                        }
                    }
                }
            }
        }


        Row summaryRow = sheet.createRow(startRow++);
        summaryRow.createCell(0).setCellValue("Total Análisis:");
        summaryRow.getCell(0).setCellStyle(styles.kpiLabel);
        summaryRow.createCell(1).setCellValue(totalAnalysis);
        summaryRow.getCell(1).setCellStyle(styles.tableData);

        startRow++;
        Row compliantRow = sheet.createRow(startRow++);
        compliantRow.createCell(0).setCellValue("✓ Cumple:");
        compliantRow.getCell(0).setCellStyle(styles.complianceGood);
        compliantRow.createCell(1).setCellValue(compliant);
        compliantRow.getCell(1).setCellStyle(styles.complianceGood);
        compliantRow.createCell(2).setCellValue(
                String.format("%.1f%%", totalAnalysis > 0 ? (compliant * 100.0 / totalAnalysis) : 0));
        compliantRow.getCell(2).setCellStyle(styles.complianceGood);

        Row nonCompliantRow = sheet.createRow(startRow++);
        nonCompliantRow.createCell(0).setCellValue("✗ NO Cumple:");
        nonCompliantRow.getCell(0).setCellStyle(styles.complianceAlert);
        nonCompliantRow.createCell(1).setCellValue(nonCompliant);
        nonCompliantRow.getCell(1).setCellStyle(styles.complianceAlert);
        nonCompliantRow.createCell(2).setCellValue(
                String.format("%.1f%%", totalAnalysis > 0 ? (nonCompliant * 100.0 / totalAnalysis) : 0));
        nonCompliantRow.getCell(2).setCellStyle(styles.complianceAlert);

        return startRow;
    }



    private void createDetailedDataSheet(Workbook workbook, String year,
                                         List<TestRequestEntity> testRequests) {
        Sheet sheet = workbook.createSheet("📄 Datos Detallados");


        DataStyles styles = createDataStyles(workbook);

        int currentRow = 0;


        currentRow = buildDataHeader(sheet, year, styles, currentRow);
        currentRow += 1; // Espacio reducido


        currentRow = buildSectionHeader(sheet, "📝 INFORMACIÓN DE SOLICITUD",
                currentRow, styles, 0, 3);
        currentRow++;


        String[] requestHeaders = {"ID Solicitud", "Fecha Creación", "Cliente", "Estado"};
        currentRow = createSectionHeaders(sheet, requestHeaders, currentRow, styles, 0);


        int sampleStartCol = 4;
        buildSectionHeader(sheet, "🧪 MUESTRA", currentRow - 2, styles, sampleStartCol, 6);
        String[] sampleHeaders = {"Código Muestra", "Identificación", "Matriz"};
        createSectionHeaders(sheet, sampleHeaders, currentRow, styles, sampleStartCol);


        int analysisStartCol = 7;
        buildSectionHeader(sheet, "🔬 ANÁLISIS Y RESULTADOS", currentRow - 2, styles,
                analysisStartCol, 14);
        String[] analysisHeaders = {"Análisis", "Método", "Equipo", "Resultado",
                "Unidad", "Cumplimiento", "Analista", "Fecha Resultado"};
        createSectionHeaders(sheet, analysisHeaders, currentRow, styles, analysisStartCol);

        currentRow++;

        currentRow = fillDetailedData(sheet, testRequests, currentRow, styles);


        applyFiltersAndFormatting(sheet, styles);

        sheet.createFreezePane(0, 4);
    }

    private int buildDataHeader(Sheet sheet, String year, DataStyles styles, int row) {
        Row titleRow = sheet.createRow(row);
        titleRow.setHeightInPoints(30);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("DATOS DETALLADOS DE AUDITORÍA " + year);
        titleCell.setCellStyle(styles.dataTitle);
        sheet.addMergedRegion(new CellRangeAddress(row, row, 0, 14));
        return row + 1;
    }

    private int buildSectionHeader(Sheet sheet, String title, int row,
                                   DataStyles styles, int startCol, int endCol) {
        Row sectionRow = sheet.getRow(row);
        if (sectionRow == null) sectionRow = sheet.createRow(row);

        Cell sectionCell = sectionRow.createCell(startCol);
        sectionCell.setCellValue(title);
        sectionCell.setCellStyle(styles.sectionHeader);
        sheet.addMergedRegion(new CellRangeAddress(row, row, startCol, endCol));

        return row + 1;
    }

    private int createSectionHeaders(Sheet sheet, String[] headers, int row,
                                     DataStyles styles, int startCol) {
        Row headerRow = sheet.getRow(row);
        if (headerRow == null) headerRow = sheet.createRow(row);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(startCol + i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(styles.columnHeader);
        }

        return row + 1;
    }

    private int fillDetailedData(Sheet sheet, List<TestRequestEntity> testRequests,
                                 int startRow, DataStyles styles) {
        int currentRow = startRow;

        for (TestRequestEntity request : testRequests) {
            String clientName = request.getCustomer() != null ?
                    request.getCustomer().getCustomerName() : "N/A";

            for (SampleEntity sample : request.getSampleEntityList()) {
                List<SampleAnalysisEntity> analysisList = sample.getAnalysisEntities();

                if (analysisList == null || analysisList.isEmpty()) {
                    // Fila sin análisis
                    Row row = sheet.createRow(currentRow++);
                    fillRequestData(row, request, clientName, styles, 0);
                    fillSampleData(row, sample, styles, 4);
                    // Celdas de análisis vacías con estilo
                    for (int i = 7; i <= 14; i++) {
                        Cell cell = row.createCell(i);
                        cell.setCellStyle(styles.emptyCell);
                    }
                } else {
                    // Filas con análisis
                    for (SampleAnalysisEntity analysis : analysisList) {
                        Row row = sheet.createRow(currentRow++);
                        fillRequestData(row, request, clientName, styles, 0);
                        fillSampleData(row, sample, styles, 4);
                        fillAnalysisData(row, analysis, styles, 7);
                    }
                }
            }
        }

        return currentRow;
    }

    private void fillRequestData(Row row, TestRequestEntity request, String clientName,
                                 DataStyles styles, int startCol) {

        Cell idCell = row.createCell(startCol);
        idCell.setCellValue(request.getRequestCode());
        idCell.setCellStyle(styles.idCell);


        Cell dateCell = row.createCell(startCol + 1);
        if (request.getCreateAt() != null) {
            dateCell.setCellValue(request.getCreateAt());
            dateCell.setCellStyle(styles.dateCell);
        } else {
            dateCell.setCellStyle(styles.normalCell);
        }


        Cell clientCell = row.createCell(startCol + 2);
        clientCell.setCellValue(clientName);
        clientCell.setCellStyle(styles.normalCell);


        Cell stateCell = row.createCell(startCol + 3);
        stateCell.setCellValue(request.getState());
        stateCell.setCellStyle(styles.statusCell);
    }

    private void fillSampleData(Row row, SampleEntity sample, DataStyles styles, int startCol) {
        Cell codeCell = row.createCell(startCol);
        codeCell.setCellValue(sample.getSampleCode());
        codeCell.setCellStyle(styles.sampleCell);

        Cell idCell = row.createCell(startCol + 1);
        idCell.setCellValue(sample.getIdentificationSample());
        idCell.setCellStyle(styles.normalCell);

        Cell matrixCell = row.createCell(startCol + 2);
        matrixCell.setCellValue(sample.getMatrix());
        matrixCell.setCellStyle(styles.normalCell);
    }

    private void fillAnalysisData(Row row, SampleAnalysisEntity analysis,
                                  DataStyles styles, int startCol) {
        ProductEntity product = analysis.getProduct();


        createStyledCell(row, startCol,
                product != null ? product.getAnalysis() : "N/A", styles.normalCell);
        createStyledCell(row, startCol + 1,
                product != null ? product.getMethod() : "N/A", styles.normalCell);
        createStyledCell(row, startCol + 2,
                product != null ? product.getEquipment() : "N/A", styles.normalCell);

        // Resultado
        Cell resultCell = row.createCell(startCol + 3);
        resultCell.setCellValue(analysis.getResultFinal());
        resultCell.setCellStyle(styles.resultCell);


        createStyledCell(row, startCol + 4, analysis.getUnit(), styles.normalCell);


        Cell complianceCell = row.createCell(startCol + 5);
        String passStatus = analysis.getPassStatus();
        complianceCell.setCellValue(passStatus);
        complianceCell.setCellStyle(
                "NO CUMPLE".equalsIgnoreCase(passStatus) ?
                        styles.nonCompliantCell : styles.compliantCell
        );


        createStyledCell(row, startCol + 6, analysis.getResultGeneratedBy(), styles.normalCell);


        Cell resDateCell = row.createCell(startCol + 7);
        if (analysis.getResultDate() != null) {
            resDateCell.setCellValue(analysis.getResultDate());
            resDateCell.setCellStyle(styles.dateCell);
        } else {
            resDateCell.setCellStyle(styles.normalCell);
        }
    }

    private void applyFiltersAndFormatting(Sheet sheet, DataStyles styles) {

        sheet.setAutoFilter(new CellRangeAddress(3, 3, 0, 14));


        int[] columnWidths = {15, 18, 25, 18, 18, 20, 18, 20, 18, 18, 15, 12, 18, 20, 18};
        for (int i = 0; i < columnWidths.length; i++) {
            sheet.setColumnWidth(i, columnWidths[i] * 256);
        }
    }

    private void createStyledCell(Row row, int col, String value, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(value != null ? value : "");
        cell.setCellStyle(style);
    }


    private static class DashboardStyles {
        CellStyle mainTitle;
        CellStyle subtitle;
        CellStyle sectionTitle;
        CellStyle kpiLabel;
        CellStyle kpiValue;
        CellStyle tableHeader;
        CellStyle tableData;
        CellStyle complianceGood;
        CellStyle complianceAlert;
    }

    private DashboardStyles createDashboardStyles(Workbook workbook) {
        DashboardStyles styles = new DashboardStyles();


        styles.mainTitle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 18);
        titleFont.setColor(IndexedColors.WHITE.getIndex());
        styles.mainTitle.setFont(titleFont);
        styles.mainTitle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        styles.mainTitle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styles.mainTitle.setAlignment(HorizontalAlignment.CENTER);
        styles.mainTitle.setVerticalAlignment(VerticalAlignment.CENTER);


        styles.subtitle = workbook.createCellStyle();
        Font subtitleFont = workbook.createFont();
        subtitleFont.setFontHeightInPoints((short) 12);
        subtitleFont.setColor(IndexedColors.GREY_50_PERCENT.getIndex());
        styles.subtitle.setFont(subtitleFont);
        styles.subtitle.setAlignment(HorizontalAlignment.CENTER);
        styles.subtitle.setVerticalAlignment(VerticalAlignment.CENTER);


        styles.sectionTitle = workbook.createCellStyle();
        Font sectionFont = workbook.createFont();
        sectionFont.setBold(true);
        sectionFont.setFontHeightInPoints((short) 14);
        sectionFont.setColor(IndexedColors.WHITE.getIndex());
        styles.sectionTitle.setFont(sectionFont);
        styles.sectionTitle.setFillForegroundColor(IndexedColors.DARK_TEAL.getIndex());
        styles.sectionTitle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styles.sectionTitle.setAlignment(HorizontalAlignment.LEFT);
        styles.sectionTitle.setVerticalAlignment(VerticalAlignment.CENTER);


        styles.kpiLabel = workbook.createCellStyle();
        Font kpiLabelFont = workbook.createFont();
        kpiLabelFont.setFontHeightInPoints((short) 10);
        kpiLabelFont.setColor(IndexedColors.GREY_80_PERCENT.getIndex());
        styles.kpiLabel.setFont(kpiLabelFont);
        styles.kpiLabel.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        styles.kpiLabel.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styles.kpiLabel.setAlignment(HorizontalAlignment.CENTER);
        styles.kpiLabel.setVerticalAlignment(VerticalAlignment.CENTER);
        styles.kpiLabel.setBorderTop(BorderStyle.THIN);
        styles.kpiLabel.setBorderBottom(BorderStyle.THIN);
        styles.kpiLabel.setBorderLeft(BorderStyle.THIN);
        styles.kpiLabel.setBorderRight(BorderStyle.THIN);


        styles.kpiValue = workbook.createCellStyle();
        Font kpiValueFont = workbook.createFont();
        kpiValueFont.setBold(true);
        kpiValueFont.setFontHeightInPoints((short) 20);
        kpiValueFont.setColor(IndexedColors.DARK_BLUE.getIndex());
        styles.kpiValue.setFont(kpiValueFont);
        styles.kpiValue.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        styles.kpiValue.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styles.kpiValue.setAlignment(HorizontalAlignment.CENTER);
        styles.kpiValue.setVerticalAlignment(VerticalAlignment.CENTER);
        styles.kpiValue.setBorderTop(BorderStyle.THIN);
        styles.kpiValue.setBorderBottom(BorderStyle.MEDIUM);
        styles.kpiValue.setBorderLeft(BorderStyle.THIN);
        styles.kpiValue.setBorderRight(BorderStyle.THIN);


        styles.tableHeader = workbook.createCellStyle();
        Font tableHeaderFont = workbook.createFont();
        tableHeaderFont.setBold(true);
        tableHeaderFont.setColor(IndexedColors.WHITE.getIndex());
        styles.tableHeader.setFont(tableHeaderFont);
        styles.tableHeader.setFillForegroundColor(IndexedColors.GREY_80_PERCENT.getIndex());
        styles.tableHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styles.tableHeader.setAlignment(HorizontalAlignment.CENTER);
        styles.tableHeader.setBorderTop(BorderStyle.THIN);
        styles.tableHeader.setBorderBottom(BorderStyle.THIN);
        styles.tableHeader.setBorderLeft(BorderStyle.THIN);
        styles.tableHeader.setBorderRight(BorderStyle.THIN);


        styles.tableData = workbook.createCellStyle();
        styles.tableData.setAlignment(HorizontalAlignment.CENTER);
        styles.tableData.setBorderTop(BorderStyle.THIN);
        styles.tableData.setBorderBottom(BorderStyle.THIN);
        styles.tableData.setBorderLeft(BorderStyle.THIN);
        styles.tableData.setBorderRight(BorderStyle.THIN);


        styles.complianceGood = workbook.createCellStyle();
        Font goodFont = workbook.createFont();
        goodFont.setBold(true);
        goodFont.setColor(IndexedColors.DARK_GREEN.getIndex());
        styles.complianceGood.setFont(goodFont);
        styles.complianceGood.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        styles.complianceGood.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styles.complianceGood.setBorderTop(BorderStyle.THIN);
        styles.complianceGood.setBorderBottom(BorderStyle.THIN);
        styles.complianceGood.setBorderLeft(BorderStyle.THIN);
        styles.complianceGood.setBorderRight(BorderStyle.THIN);


        styles.complianceAlert = workbook.createCellStyle();
        Font alertFont = workbook.createFont();
        alertFont.setBold(true);
        alertFont.setColor(IndexedColors.DARK_RED.getIndex());
        styles.complianceAlert.setFont(alertFont);
        styles.complianceAlert.setFillForegroundColor(IndexedColors.ROSE.getIndex());
        styles.complianceAlert.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styles.complianceAlert.setBorderTop(BorderStyle.THIN);
        styles.complianceAlert.setBorderBottom(BorderStyle.THIN);
        styles.complianceAlert.setBorderLeft(BorderStyle.THIN);
        styles.complianceAlert.setBorderRight(BorderStyle.THIN);

        return styles;
    }


    private static class DataStyles {
        CellStyle dataTitle;
        CellStyle sectionHeader;
        CellStyle columnHeader;
        CellStyle normalCell;
        CellStyle idCell;
        CellStyle dateCell;
        CellStyle statusCell;
        CellStyle sampleCell;
        CellStyle resultCell;
        CellStyle compliantCell;
        CellStyle nonCompliantCell;
        CellStyle emptyCell;
    }

    private DataStyles createDataStyles(Workbook workbook) {
        DataStyles styles = new DataStyles();


        styles.dataTitle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 14);
        titleFont.setColor(IndexedColors.WHITE.getIndex());
        styles.dataTitle.setFont(titleFont);
        styles.dataTitle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        styles.dataTitle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styles.dataTitle.setAlignment(HorizontalAlignment.CENTER);
        styles.dataTitle.setVerticalAlignment(VerticalAlignment.CENTER);


        styles.sectionHeader = workbook.createCellStyle();
        Font sectionFont = workbook.createFont();
        sectionFont.setBold(true);
        sectionFont.setFontHeightInPoints((short) 11);
        sectionFont.setColor(IndexedColors.WHITE.getIndex());
        styles.sectionHeader.setFont(sectionFont);
        styles.sectionHeader.setFillForegroundColor(IndexedColors.SEA_GREEN.getIndex());
        styles.sectionHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styles.sectionHeader.setAlignment(HorizontalAlignment.CENTER);
        styles.sectionHeader.setBorderTop(BorderStyle.MEDIUM);
        styles.sectionHeader.setBorderBottom(BorderStyle.MEDIUM);
        styles.sectionHeader.setBorderLeft(BorderStyle.MEDIUM);
        styles.sectionHeader.setBorderRight(BorderStyle.MEDIUM);


        styles.columnHeader = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 10);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        styles.columnHeader.setFont(headerFont);
        styles.columnHeader.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        styles.columnHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styles.columnHeader.setAlignment(HorizontalAlignment.CENTER);
        styles.columnHeader.setWrapText(true);
        styles.columnHeader.setBorderTop(BorderStyle.THIN);
        styles.columnHeader.setBorderBottom(BorderStyle.MEDIUM);
        styles.columnHeader.setBorderLeft(BorderStyle.THIN);
        styles.columnHeader.setBorderRight(BorderStyle.THIN);


        styles.normalCell = workbook.createCellStyle();
        styles.normalCell.setBorderTop(BorderStyle.THIN);
        styles.normalCell.setBorderBottom(BorderStyle.THIN);
        styles.normalCell.setBorderLeft(BorderStyle.THIN);
        styles.normalCell.setBorderRight(BorderStyle.THIN);
        styles.normalCell.setWrapText(false);


        styles.idCell = workbook.createCellStyle();
        Font idFont = workbook.createFont();
        idFont.setBold(true);
        idFont.setColor(IndexedColors.DARK_BLUE.getIndex());
        styles.idCell.setFont(idFont);
        styles.idCell.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        styles.idCell.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styles.idCell.setBorderTop(BorderStyle.THIN);
        styles.idCell.setBorderBottom(BorderStyle.THIN);
        styles.idCell.setBorderLeft(BorderStyle.THIN);
        styles.idCell.setBorderRight(BorderStyle.THIN);


        styles.dateCell = workbook.createCellStyle();
        styles.dateCell.setBorderTop(BorderStyle.THIN);
        styles.dateCell.setBorderBottom(BorderStyle.THIN);
        styles.dateCell.setBorderLeft(BorderStyle.THIN);
        styles.dateCell.setBorderRight(BorderStyle.THIN);
        styles.dateCell.setDataFormat(workbook.createDataFormat().getFormat("dd/mm/yyyy"));


        styles.statusCell = workbook.createCellStyle();
        styles.statusCell.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        styles.statusCell.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styles.statusCell.setAlignment(HorizontalAlignment.CENTER);
        styles.statusCell.setBorderTop(BorderStyle.THIN);
        styles.statusCell.setBorderBottom(BorderStyle.THIN);
        styles.statusCell.setBorderLeft(BorderStyle.THIN);
        styles.statusCell.setBorderRight(BorderStyle.THIN);


        styles.sampleCell = workbook.createCellStyle();
        Font sampleFont = workbook.createFont();
        sampleFont.setColor(IndexedColors.DARK_GREEN.getIndex());
        styles.sampleCell.setFont(sampleFont);
        styles.sampleCell.setBorderTop(BorderStyle.THIN);
        styles.sampleCell.setBorderBottom(BorderStyle.THIN);
        styles.sampleCell.setBorderLeft(BorderStyle.THIN);
        styles.sampleCell.setBorderRight(BorderStyle.THIN);


        styles.resultCell = workbook.createCellStyle();
        Font resultFont = workbook.createFont();
        resultFont.setBold(true);
        styles.resultCell.setFont(resultFont);
        styles.resultCell.setAlignment(HorizontalAlignment.CENTER);
        styles.resultCell.setBorderTop(BorderStyle.THIN);
        styles.resultCell.setBorderBottom(BorderStyle.THIN);
        styles.resultCell.setBorderLeft(BorderStyle.THIN);
        styles.resultCell.setBorderRight(BorderStyle.THIN);


        styles.compliantCell = workbook.createCellStyle();
        Font compliantFont = workbook.createFont();
        compliantFont.setBold(true);
        compliantFont.setColor(IndexedColors.DARK_GREEN.getIndex());
        styles.compliantCell.setFont(compliantFont);
        styles.compliantCell.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        styles.compliantCell.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styles.compliantCell.setAlignment(HorizontalAlignment.CENTER);
        styles.compliantCell.setBorderTop(BorderStyle.THIN);
        styles.compliantCell.setBorderBottom(BorderStyle.THIN);
        styles.compliantCell.setBorderLeft(BorderStyle.THIN);
        styles.compliantCell.setBorderRight(BorderStyle.THIN);


        styles.nonCompliantCell = workbook.createCellStyle();
        Font nonCompliantFont = workbook.createFont();
        nonCompliantFont.setBold(true);
        nonCompliantFont.setColor(IndexedColors.WHITE.getIndex());
        styles.nonCompliantCell.setFont(nonCompliantFont);
        styles.nonCompliantCell.setFillForegroundColor(IndexedColors.RED.getIndex());
        styles.nonCompliantCell.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styles.nonCompliantCell.setAlignment(HorizontalAlignment.CENTER);
        styles.nonCompliantCell.setBorderTop(BorderStyle.THIN);
        styles.nonCompliantCell.setBorderBottom(BorderStyle.THIN);
        styles.nonCompliantCell.setBorderLeft(BorderStyle.THIN);
        styles.nonCompliantCell.setBorderRight(BorderStyle.THIN);


        styles.emptyCell = workbook.createCellStyle();
        styles.emptyCell.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        styles.emptyCell.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styles.emptyCell.setBorderTop(BorderStyle.THIN);
        styles.emptyCell.setBorderBottom(BorderStyle.THIN);
        styles.emptyCell.setBorderLeft(BorderStyle.THIN);
        styles.emptyCell.setBorderRight(BorderStyle.THIN);

        return styles;
    }
}
