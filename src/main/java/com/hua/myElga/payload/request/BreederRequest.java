package com.hua.myElga.payload.request;

public class BreederRequest {

    private int killedAnimals;
    private int totalAnimals;
    private Double pricePerUnit;

    public BreederRequest(){}

    public BreederRequest(int killedAnimals, int totalAnimals, Double pricePerUnit) {
        this.killedAnimals = killedAnimals;
        this.totalAnimals = totalAnimals;
        this.pricePerUnit = pricePerUnit;
    }

    public int getKilledAnimals() {
        return killedAnimals;
    }
    public void setKilledAnimals(int killedAnimals) {
        this.killedAnimals = killedAnimals;
    }
    public int getTotalAnimals() {
        return totalAnimals;
    }
    public void setTotalAnimals(int totalAnimals) {
        this.totalAnimals = totalAnimals;
    }
    public Double getPricePerUnit() {
        return pricePerUnit;
    }
    public void setPricePerUnit(Double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }
}
