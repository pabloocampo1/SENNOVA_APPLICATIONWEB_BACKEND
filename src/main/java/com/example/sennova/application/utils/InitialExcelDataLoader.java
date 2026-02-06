package com.example.sennova.application.utils;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * Clase temporal para importar datos desde un Excel al iniciar la app.
 * Una vez completada la importación, puedes eliminarla o comentar @Component.
// */

// implements CommandLineRunner
// @Component
public class InitialExcelDataLoader  {
//
//    private final ExcelImportService excelImportService;
//
//    public InitialExcelDataLoader(ExcelImportService excelImportService) {
//        this.excelImportService = excelImportService;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//
//        var resource = new ClassPathResource("data/equipos.xlsx");
//
//        if (resource.exists()) {
//            try (InputStream inputStream = resource.getInputStream()) {
//                var multipartFile = new MockMultipartFile(
//                        "file",
//                        "equipos.xlsx",
//                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
//                        inputStream
//                );
//
//                excelImportService.importProductsFromExcel(multipartFile);
//                System.out.println("✅ Datos cargados correctamente desde Excel.");
//            }
//        } else {
//            System.err.println("⚠️ No se encontró el archivo equipos.xlsx en resources/data/");
//        }
//    }
}
