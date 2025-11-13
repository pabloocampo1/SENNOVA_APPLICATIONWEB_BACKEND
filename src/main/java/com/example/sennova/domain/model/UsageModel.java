package com.example.sennova.domain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public class UsageModel {

    private Long equipmentUsageId;

    @NotBlank(message = "El nombre del uso es obligatorio")
    @Size(max = 150, message = "El nombre del uso no puede superar los 150 caracteres")
    private String usageName;

    private LocalDate createAt;
    private LocalDate updateAt;

    private List<EquipmentModel> equipmentList;

    // Getters & Setters
    public Long getEquipmentUsageId() {
        return equipmentUsageId;
    }

    public void setEquipmentUsageId(Long equipmentUsageId) {
        this.equipmentUsageId = equipmentUsageId;
    }

    public String getUsageName() {
        return usageName;
    }

    public void setUsageName(String usageName) {
        this.usageName = usageName;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }

    public LocalDate getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDate updateAt) {
        this.updateAt = updateAt;
    }

    public List<EquipmentModel> getEquipmentList() {
        return equipmentList;
    }

    public void setEquipmentList(List<EquipmentModel> equipmentList) {
        this.equipmentList = equipmentList;
    }
}
