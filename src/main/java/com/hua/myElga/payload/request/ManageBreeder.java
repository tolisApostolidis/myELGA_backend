package com.hua.myElga.payload.request;

public class ManageBreeder {
    private float damagePercentage;
    private float compensationFactor;
    private Double residualValue;
    private String state;
    public ManageBreeder(){}

    public ManageBreeder(float damagePercentage, float compensationFactor, Double residualValue, String state) {
        this.damagePercentage = damagePercentage;
        this.compensationFactor = compensationFactor;
        this.residualValue = residualValue;
        this.state = state;
    }

    public float getDamagePercentage() {
        return damagePercentage;
    }
    public void setDamagePercentage(float damagePercentage) {
        this.damagePercentage = damagePercentage;
    }

    public float getCompensationFactor() {
        return compensationFactor;
    }
    public void setCompensationFactor(float compensationFactor) {
        this.compensationFactor = compensationFactor;
    }

    public Double getResidualValue() {
        return residualValue;
    }
    public void setResidualValue(Double residualValue) {
        this.residualValue = residualValue;
    }

    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
}
