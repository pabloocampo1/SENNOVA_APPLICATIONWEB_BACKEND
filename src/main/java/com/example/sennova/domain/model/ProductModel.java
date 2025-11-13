package com.example.sennova.domain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class ProductModel {


    private Long productId;

    @NotBlank(message = "El análisis no puede estar vacío")
    private String analysis;

    @NotBlank(message = "La matriz no puede estar vacía")
    private String matrix;

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


    public ProductModel() {
    }

    public ProductModel(Long productId, String analysis, String matrix, String method, String equipment, String units, double price, String notes, LocalDate createAt, LocalDate updateAt) {
        this.productId = productId;
        this.analysis = analysis;
        this.matrix = matrix;
        this.method = method;
        this.equipment = equipment;
        this.units = units;
        this.price = price;
        this.notes = notes;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public String getMatrix() {
        return matrix;
    }

    public void setMatrix(String matrix) {
        this.matrix = matrix;
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
