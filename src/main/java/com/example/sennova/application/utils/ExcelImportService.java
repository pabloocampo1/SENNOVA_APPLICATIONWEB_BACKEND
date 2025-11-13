package com.example.sennova.application.utils;

import com.example.sennova.domain.constants.EquipmentConstants;
import com.example.sennova.infrastructure.persistence.entities.LocationEntity;
import com.example.sennova.infrastructure.persistence.entities.UsageEntity;
import com.example.sennova.infrastructure.persistence.entities.UserEntity;
import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.EquipmentEntity;
import com.example.sennova.infrastructure.persistence.repositoryJpa.*;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExcelImportService {

    private final EquipmentRepositoryJpa equipmentRepositoryJpa;
    private final LocationPersistenceJpa locationPersistenceJpa;
    private final UsageRepositoryJpa usageRepositoryJpa;
    private final UserRepositoryJpa userRepositoryJpa;

    public ExcelImportService(EquipmentRepositoryJpa productRepository, LocationPersistenceJpa locationPersistenceJpa, UsageRepositoryJpa usageRepositoryJpa, UserRepositoryJpa userRepositoryJpa) {
        this.equipmentRepositoryJpa = productRepository;
        this.locationPersistenceJpa = locationPersistenceJpa;

        this.usageRepositoryJpa = usageRepositoryJpa;
        this.userRepositoryJpa = userRepositoryJpa;
    }

    @Transactional
    public void importProductsFromExcel(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = 0;

            for (Row row : sheet) {

                if (rowCount++ == 0) continue;
                if (row == null) continue;
                if (row.getCell(0) == null || getCellValue(row.getCell(0)) == null) continue;
                if (rowCount++ == 0) continue;

                EquipmentEntity equipment = new EquipmentEntity();


                equipment.setInternalCode(getCellValue(row.getCell(0)));
                String name = getCellValue(row.getCell(1));
                if (name == null || name.isBlank()) {
                    System.out.println("⚠️  Skipping row " + row.getRowNum() + " because 'equipment_name' is null.");
                    continue;
                }
                equipment.setEquipmentName(name);
                equipment.setBrand(getCellValue(row.getCell(2)));
                equipment.setModel(getCellValue(row.getCell(3)));
                equipment.setSerialNumber(getCellValue(row.getCell(4)));
                equipment.setSenaInventoryTag(getCellValue(row.getCell(5)));
                equipment.setState(getCellValue(row.getCell(6)));


                String stateValue = getCellValue(row.getCell(6));

                if (stateValue == null || stateValue.trim().isEmpty()) {

                    equipment.setState(EquipmentConstants.STATUS_ACTIVE);
                    equipment.setAvailable(true);
                } else {
                    equipment.setState(stateValue.trim());


                    if (stateValue.equalsIgnoreCase(EquipmentConstants.STATUS_OUT_OF_SERVICE)
                            || stateValue.equalsIgnoreCase(EquipmentConstants.STATUS_DECOMMISSIONED)) {
                        equipment.setAvailable(false);
                    } else {
                        equipment.setAvailable(true);
                    }
                }



                String locationName = getCellValue(row.getCell(7));
                List<LocationEntity> locations = locationPersistenceJpa.findByLocationName(locationName);
                LocationEntity location = locations.isEmpty() ? null : locations.get(0);
                equipment.setLocation(location);


                equipment.setDescription(getCellValue(row.getCell(8)));

                String usageName = getCellValue(row.getCell(9));
                List<UsageEntity> usages = usageRepositoryJpa.findByUsageName(usageName);
                UsageEntity usage = usages.isEmpty() ? null : usages.get(0);
                equipment.setUsage(usage);

                equipment.setAcquisitionDate(parseDate(getCellValue(row.getCell(10))));

                equipment.setAmperage(getCellValue(row.getCell(11)));
                equipment.setVoltage(getCellValue(row.getCell(12)));


                equipment.setEquipmentCost(parseCost(getCellValue(row.getCell(13))));

                String responsableName = getCellValue(row.getCell(14));
                List<UserEntity> users = userRepositoryJpa.findAllByNameContainingIgnoreCase(responsableName);
                UserEntity responsable = users.isEmpty() ? null : users.get(0);
                equipment.setResponsible(responsable);

                equipment.setMarkReport(false);
                equipment.setMaintenanceDate(LocalDate.now().plusMonths(11));
                equipment.setCreateAt(LocalDateTime.now());
                equipment.setUpdateAt(LocalDateTime.now());


                equipmentRepositoryJpa.save(equipment);
            }

            System.out.println("✅ Excel import completed successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("❌ Error processing Excel: " + e.getMessage());
        }
    }


    private String getCellValue(Cell cell) {
        if (cell == null) return null;
        cell.setCellType(CellType.STRING);
        String value = cell.getStringCellValue().trim();
        return value.isEmpty() ? null : value;
    }


    private LocalDate parseDate(String value) {
        try {
            return LocalDate.parse(value.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private double parseCost(String value) {
        try {
            String cleaned = value.replaceAll("[^\\d.,]", "").replace(",", ".");
            return Double.parseDouble(cleaned);
        } catch (Exception e) {
            return 0;
        }
    }
}
