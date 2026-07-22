package com.hua.myElga.payload;

public class PdfFileFields {
    private String applicationType;
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private Long phoneNumber;
    private Long afm;

    //// Submission
    private int fieldArea;
    private float averageProduction;

    //// Breeder
    private int killedAnimals;
    private int totalAnimals;
    private float compensationFactor;
    private Double residualValue;

    //// Common
    private Double pricePerUnit;
    private float damagePercentage;
    private String creationDate;
    private Double damages;

    //// Constructors one for each type

    public PdfFileFields(String applicationType, Long id, String firstName, String lastName, String address, Long phoneNumber, Long afm,
                         int fieldArea, float averageProduction, Double pricePerUnit, float damagePercentage, String creationDate, Double damages) {
        this.applicationType = applicationType;
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.afm = afm;
        this.fieldArea = fieldArea;
        this.averageProduction = averageProduction;
        this.pricePerUnit = pricePerUnit;
        this.damagePercentage = damagePercentage;
        this.creationDate = creationDate;
        this.damages = damages;
    }

    public PdfFileFields(String applicationType, Long id, String firstName, String lastName, String address, Long phoneNumber, Long afm,
                         int killedAnimals, int totalAnimals, float compensationFactor, Double residualValue, Double pricePerUnit, float damagePercentage, String creationDate, Double damages) {
        this.applicationType = applicationType;
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.afm = afm;
        this.killedAnimals = killedAnimals;
        this.totalAnimals = totalAnimals;
        this.compensationFactor = compensationFactor;
        this.residualValue = residualValue;
        this.pricePerUnit = pricePerUnit;
        this.damagePercentage = damagePercentage;
        this.creationDate = creationDate;
        this.damages = damages;
    }

    public String getApplicationType() {
        return applicationType;
    }
    public Long getId() {
        return id;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getAddress() {
        return address;
    }
    public Long getPhoneNumber() {
        return phoneNumber;
    }
    public Long getAfm() {
        return afm;
    }
    public int getFieldArea() {
        return fieldArea;
    }
    public float getAverageProduction() {
        return averageProduction;
    }
    public int getKilledAnimals() {
        return killedAnimals;
    }
    public int getTotalAnimals() {
        return totalAnimals;
    }
    public float getCompensationFactor() {
        return compensationFactor;
    }
    public Double getResidualValue() {
        return residualValue;
    }
    public Double getPricePerUnit() {
        return pricePerUnit;
    }
    public float getDamagePercentage() {
        return damagePercentage;
    }
    public String getCreationDate() {
        return creationDate;
    }
    public Double getDamages() {
        return damages;
    }
}



