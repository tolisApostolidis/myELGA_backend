package com.hua.myElga.payload.response;

public class BreedersResponse {

    private Long id;
    private String creationDate;
    private int totalAnimals;
    private String state;
    private Double residualValue;
    private Double pricePerUnit;
    private int numberOfKilledAnimals;
    private float coveragePercentage;
    private float compensationFactor;
    private Double damages;

    public BreedersResponse(){}
    public BreedersResponse(Long id, String creationDate, int totalAnimals, String state, Double residualValue, Double pricePerUnit, int numberOfKilledAnimals, float coveragePercentage, float compensationFactor, Double damages) {
        this.id = id;
        this.creationDate = creationDate;
        this.totalAnimals = totalAnimals;
        this.state = state;
        this.residualValue = residualValue;
        this.pricePerUnit = pricePerUnit;
        this.numberOfKilledAnimals = numberOfKilledAnimals;
        this.coveragePercentage = coveragePercentage;
        this.compensationFactor = compensationFactor;
        this.damages = damages;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public int getTotalAnimals() {
        return totalAnimals;
    }

    public void setTotalAnimals(int totalAnimals) {
        this.totalAnimals = totalAnimals;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Double getResidualValue() {
        return residualValue;
    }

    public void setResidualValue(Double residualValue) {
        this.residualValue = residualValue;
    }

    public Double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(Double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public int getNumberOfKilledAnimals() {
        return numberOfKilledAnimals;
    }

    public void setNumberOfKilledAnimals(int numberOfKilledAnimals) {
        this.numberOfKilledAnimals = numberOfKilledAnimals;
    }

    public float getCoveragePercentage() {
        return coveragePercentage;
    }

    public void setCoveragePercentage(float coveragePercentage) {
        this.coveragePercentage = coveragePercentage;
    }

    public float getCompensationFactor() {
        return compensationFactor;
    }

    public void setCompensationFactor(float compensationFactor) {
        this.compensationFactor = compensationFactor;
    }

    public Double getDamages() {
        return damages;
    }

    public void setDamages(Double damages) {
        this.damages = damages;
    }
}
