package com.example.sennova.application.usecasesImpl;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    public byte[] generatePdfFinalResultTestRequest() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Vista previa del resultado del ensayo").setBold());

            Table table = new Table(new float[]{3, 3, 3});
            table.addCell("Codigo muestra");
            table.addCell("Análisis");
            table.addCell("Resultado");

            table.addCell("M-001");
            table.addCell("pH");
            table.addCell("7.2");

            table.addCell("M-002");
            table.addCell("Color");
            table.addCell("Claro");

            document.add(table);
            document.close();

            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generando PDF", e);
        }
    }
}
