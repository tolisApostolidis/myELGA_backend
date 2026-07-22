package com.hua.myElga.entity;

import jakarta.persistence.*;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "breeder_submission")
public class BreederSubmission {
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("MM/dd/yyyy");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private int numberOfKilledAnimals;
    @Column
    private int totalAnimals;
    @Column(columnDefinition = "Float(5,2)")
    private float damageCoveragePercentage;
    @Column(columnDefinition = "Float(5,2)")
    private float compensationFactor;
    @Column(columnDefinition = "Double(7,2)")
    private Double pricePerUnit;
    @Column(columnDefinition = "Double(7,2)")
    private Double residualValue;
    @Column
    private String state;
    @Column
    @Temporal(TemporalType.DATE)
    private Date creationDate;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "farmer_id")
    private Farmer farmer;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "compensation_id", referencedColumnName = "id")
    private Damages damages;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "file_id")
    private FileApplication fileApplication;

    // Constructor -- sets STATE field to 'CREATED'
    public BreederSubmission(){
        this.state = "CREATED";
    }
    //// Getters - Setters ////
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public int getNumberOfKilledAnimals() {
        return numberOfKilledAnimals;
    }
    public void setNumberOfKilledAnimals(int numberOfKilledAnimals) {
        this.numberOfKilledAnimals = numberOfKilledAnimals;
    }

    public int getTotalAnimals() {
        return totalAnimals;
    }
    public void setTotalAnimals(int totalAnimals) {
        this.totalAnimals = totalAnimals;
    }

    public float getDamageCoveragePercentage() {
        return damageCoveragePercentage;
    }
    public void setDamageCoveragePercentage(float damageCoveragePercentage) {
        this.damageCoveragePercentage = damageCoveragePercentage;
    }

    public float getCompensationFactor() {
        return compensationFactor;
    }
    public void setCompensationFactor(float compensationFactor) {
        this.compensationFactor = compensationFactor;
    }

    public Double getPricePerUnit() {
        return pricePerUnit;
    }
    public void setPricePerUnit(Double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
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

    public String getCreationDate() {
        SimpleDateFormat FORMATTER = new SimpleDateFormat("dd-MM-yyyy");

        return creationDate != null ? FORMATTER.format(creationDate) : null;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Damages getDamages() {
        return damages;
    }
    public void setDamages(Damages damages) {
        this.damages = damages;
    }

    public Farmer getFarmer() {
        return farmer;
    }
    public void setFarmer(Farmer farmer) {
        this.farmer = farmer;
    }

    public FileApplication getFileApplication() {
        return fileApplication;
    }
    public void setFileApplication(FileApplication fileApplication) {
        this.fileApplication = fileApplication;
    }

    //// Method that calculates and return the Damages compensation sum ////
    public Double calculateCompensation(float damageCoveragePercentage, float compensationFactor, Double residualValue) {
        double exp = (((double) numberOfKilledAnimals / totalAnimals) - damageCoveragePercentage);
        double total;
        if (exp <= 0) {
            total = (double) ( numberOfKilledAnimals * compensationFactor * pricePerUnit ) - residualValue;
        }else  {
            total = ( exp * totalAnimals * compensationFactor * pricePerUnit ) - residualValue;
        }
        total = total > 150000 ? 150000 : total;
        String totalToString = new DecimalFormat("#.##").format(total);
        totalToString = totalToString.replace(",", ".");
        total = Double.parseDouble(totalToString);

        return total;
    }
}
