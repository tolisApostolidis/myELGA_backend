package com.hua.myElga.payload.response;

import java.util.Date;

public class SubmissionsResponse {
    private Long id;
    private float fieldArea;
    private float averageProduction;
    private Double pricePerProductUnit;
    private String description;
    private float damagePercentage;
    private String state;
    private String creationDate;
    private Double damages;
    public SubmissionsResponse(Long id, float fieldArea, float averageProduction, Double pricePerProductUnit, String description, float damagePercentage, String state, String creationDate, Double damages) {
        this.id = id;
        this.fieldArea = fieldArea;
        this.averageProduction = averageProduction;
        this.pricePerProductUnit = pricePerProductUnit;
        this.description = description;
        this.damagePercentage =damagePercentage;
        this.state = state;
        this.creationDate = creationDate;
        this.damages = damages;
    }

    public Long getId() {
        return id;
    }

    public float getFieldArea() {
        return fieldArea;
    }

    public float getAverageProduction() {
        return averageProduction;
    }

    public Double getPricePerProductUnit() {
        return pricePerProductUnit;
    }

    public String getDescription() {
        return description;
    }

    public float getDamagePercentage() {
        return damagePercentage;
    }

    public String getState() {
        return state;
    }

    public String getCreationDate() {
        return creationDate;
    }
    public Double getDamages() {
        return this.damages;
    }
}
