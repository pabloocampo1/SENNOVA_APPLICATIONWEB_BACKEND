package com.example.sennova.domain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public class AnalysisModel {


    private Long analysisId;

    @NotBlank(message = "El análisis no puede estar vacío")
    private String analysisName;

    private Boolean available;


    @NotBlank(message = "El método no puede estar vacío")
    private String method;

    @NotBlank(message = "El equipo no puede estar vacío")
    private String equipment;

    @NotBlank(message = "Las unidades no pueden estar vacías")
    private String units;

    @Positive(message = "El precio debe ser mayor a 0")
    private double price;

    @Size(max = 500, message = "Las notas no pueden superar los 500 caracteres")
    private String notes;

    private LocalDate createAt;
    private LocalDate updateAt;

    List<MatrixModel> matrices;
    List<UserModel> qualifiedUsers;


    public AnalysisModel() {
    }

    public AnalysisModel(Long analysisId, String analysisName, String method, String equipment, String units, double price, String notes, LocalDate createAt, LocalDate updateAt) {
        this.analysisId = analysisId;
        this.analysisName = analysisName;
        this.method = method;
        this.equipment = equipment;
        this.units = units;
        this.price = price;
        this.notes = notes;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }


    public AnalysisModel(Long analysisId, String analysisName, Boolean available, String method, String equipment, String units, double price, String notes, LocalDate createAt, LocalDate updateAt, List<MatrixModel> matrices, List<UserModel> qualifiedUsers) {
        this.analysisId = analysisId;
        this.analysisName = analysisName;
        this.available = available;
        this.method = method;
        this.equipment = equipment;
        this.units = units;
        this.price = price;
        this.notes = notes;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.matrices = matrices;
        this.qualifiedUsers = qualifiedUsers;
    }

    public void removeUser(Long userId){
        if(userId != null){
             this.getQualifiedUsers().removeIf(u -> u.getUserId().equals(userId));
        }
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public List<MatrixModel> getMatrices() {
        return matrices;
    }

    public void setMatrices(List<MatrixModel> matrices) {
        this.matrices = matrices;
    }

    public List<UserModel> getQualifiedUsers() {
        return qualifiedUsers;
    }

    public void setQualifiedUsers(List<UserModel> qualifiedUsers) {
        this.qualifiedUsers = qualifiedUsers;
    }

    public Long getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(Long analysisId) {
        this.analysisId = analysisId;
    }

    public String getAnalysisName() {
        return analysisName;
    }

    public void setAnalysisName(String analysisName) {
        this.analysisName = analysisName;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
}
