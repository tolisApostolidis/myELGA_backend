package com.hua.myElga.payload.request;

public class ManageSubmission {

    private float damagePercentage;
    private Double pricePerUnit;
    private String state;

    public ManageSubmission(){}

    public ManageSubmission(float damagePercentage, Double pricePerUnit, String state) {
        this.damagePercentage = damagePercentage;
        this.pricePerUnit = pricePerUnit;
        this.state = state;
    }

    public float getDamagePercentage() {
        return damagePercentage;
    }

    public void setDamagePercentage(float damagePercentage) {
        this.damagePercentage = damagePercentage;
    }

    public Double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(Double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
