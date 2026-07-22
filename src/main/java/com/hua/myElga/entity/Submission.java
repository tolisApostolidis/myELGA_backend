package com.hua.myElga.entity;

import jakarta.persistence.*;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "submission")
public class Submission {
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("MM/dd/yyyy");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int fieldArea;
    @Column(columnDefinition = "Float(6,2)")
    private float averageProduction;
    @Column(columnDefinition = "Double(7,2)")
    private Double pricePerUnit;
    @Column(columnDefinition = "Float(5,2)")
    private float damagePercentage;
    @Column
    private String state;
    @Column
    private String description;
    @Temporal(TemporalType.DATE)
    @Column
    private Date creationDate;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "farmer_id")
    private Farmer farmer;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "file_id")
    private FileApplication fileApplication;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "compensation_id", referencedColumnName = "id")
    private Damages damages;

    public Submission(){
        this.state = "CREATED";
    }

    //// Getters and Setters ////
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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

    public Double getPricePerUnit() {
        return pricePerUnit;
    }
    public void setPricePerProductUnit(Double pricePerProductUnit) {
        this.pricePerUnit = pricePerProductUnit;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public float getDamagePercentage() {
        return damagePercentage;
    }
    public void setDamagePercentage(float damagePercentage) {
        this.damagePercentage = damagePercentage;
    }

    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

    public String getCreationDate() {
        SimpleDateFormat FORMATTER = new SimpleDateFormat("dd-MM-yyyy");

        return creationDate != null ? FORMATTER.format(creationDate) : null;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Farmer getFarmer() {
        return farmer;
    }
    public void setFarmer(Farmer farmer) {
        this.farmer = farmer;
    }

    public Damages getDamages() {
        return damages;
    }
    public void setDamages(Damages damages) {
        this.damages = damages;
    }

    public FileApplication getFileApplication() {
        return fileApplication;
    }
    public void setFileApplication(FileApplication fileApplication) {
        this.fileApplication = fileApplication;
    }

    //// Function that calculates Damages compensation sum ////
    public Double calculateCompensation(float damagePercentage, Double pricePerUnit) {
        Double total;
        if (damagePercentage - 15 <= 0) {
            total = fieldArea * averageProduction * 15 * 0.88 / pricePerUnit;
        }else {
            total = fieldArea * averageProduction * (damagePercentage - 15) * 0.88 / pricePerUnit;
        }
        total = total > 150000 ? 150000 : total;
        String totalToString = new DecimalFormat("#.##").format(total);
        totalToString = totalToString.replace(",", ".");
        total = Double.parseDouble(totalToString);

        return total;
    }
}
