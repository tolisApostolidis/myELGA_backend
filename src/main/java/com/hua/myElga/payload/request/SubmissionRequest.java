package com.hua.myElga.payload.request;

public class SubmissionRequest {

    private int fieldArea;
    private float averageProduction;
    private String description;

    public SubmissionRequest(){}
    public SubmissionRequest(int fieldArea, float averageProduction, String description) {
        this.fieldArea = fieldArea;
        this.averageProduction = averageProduction;
        this.description = description;
    }

    public int getFieldArea() {
        return fieldArea;
    }
    public void setFieldArea(int fieldArea) {
        this.fieldArea = fieldArea;
    }
    public float getAverageProduction() {
        return averageProduction;
    }
    public void setAverageProduction(float averageProduction) {
        this.averageProduction = averageProduction;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
