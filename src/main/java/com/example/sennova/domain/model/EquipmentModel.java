package com.example.sennova.domain.model;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class EquipmentModel {
    private Long equipmentId;

    @NotBlank(message = "El código interno no puede estar vacío")
    @Size(max = 100, message = "El código interno no puede superar los 100 caracteres")
    private String internalCode;

    @NotBlank(message = "El nombre del equipo es obligatorio")
    private String equipmentName;

    @Size(max = 100, message = "La marca no puede superar los 100 caracteres")
    private String brand;

    @Size(max = 100, message = "El modelo no puede superar los 100 caracteres")
    private String model;

    private String imageUrl;

    private String senaInventoryTag;

    @NotNull(message = "El número de serie es obligatorio")
    @Positive(message = "El número de serie debe ser positivo")
    private String serialNumber;

    @PastOrPresent(message = "La fecha de adquisición no puede estar en el futuro")
    private LocalDate acquisitionDate;

    @Size(max = 50, message = "El amperaje no puede superar los 50 caracteres")
    private String amperage;

    @Size(max = 50, message = "El voltaje no puede superar los 50 caracteres")
    private String voltage;

    @PositiveOrZero(message = "El costo del equipo no puede ser negativo")
    private double equipmentCost;

    private String state;

    private String description;

    private Boolean markReport;

    private Boolean available;

    private UserModel responsible;

    private LocationModel location;

    private UsageModel usage;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    private LocalDate maintenanceDate;

    public Boolean getMarkReport() {
        return markReport;
    }

    public void setMarkReport(Boolean markReport) {
        this.markReport = markReport;
    }

    public String getSenaInventoryTag() {
        return senaInventoryTag;
    }

    public void setSenaInventoryTag(String senaInventoryTag) {
        this.senaInventoryTag = senaInventoryTag;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getInternalCode() {
        return internalCode;
    }

    public void setInternalCode(String internalCode) {
        this.internalCode = internalCode;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public LocalDate getAcquisitionDate() {
        return acquisitionDate;
    }

    public void setAcquisitionDate(LocalDate acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public String getAmperage() {
        return amperage;
    }

    public void setAmperage(String amperage) {
        this.amperage = amperage;
    }

    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }

    public double getEquipmentCost() {
        return equipmentCost;
    }

    public void setEquipmentCost(double equipmentCost) {
        this.equipmentCost = equipmentCost;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public UserModel getResponsible() {
        return responsible;
    }

    public void setResponsible(UserModel responsible) {
        this.responsible = responsible;
    }

    public LocationModel getLocation() {
        return location;
    }

    public void setLocation(LocationModel location) {
        this.location = location;
    }

    public UsageModel getUsage() {
        return usage;
    }

    public void setUsage(UsageModel usage) {
        this.usage = usage;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public LocalDate getMaintenanceDate() {
        return maintenanceDate;
    }

    public void setMaintenanceDate(LocalDate maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
    }
}
