package com.example.sennova.domain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public class LocationModel {

    private Long equipmentLocationId;

    @NotBlank(message = "El nombre de la ubicación es obligatorio")
    @Size(max = 150, message = "El nombre de la ubicación no puede superar los 150 caracteres")
    private String locationName;

    private LocalDate createAt;
    private LocalDate updateAt;

    private List<EquipmentModel> equipmentList;

    // Getters & Setters
    public Long getEquipmentLocationId() {
        return equipmentLocationId;
    }

    public void setEquipmentLocationId(Long equipmentLocationId) {
        this.equipmentLocationId = equipmentLocationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
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
