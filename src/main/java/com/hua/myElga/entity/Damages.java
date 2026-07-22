package com.hua.myElga.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "damages")
public class Damages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(columnDefinition = "DOUBLE(8,2)")
    private Double damagesSum;

    public Damages() {}
    public Damages(Double damagesSum) {
        this.damagesSum = damagesSum;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Double getDamagesSum() {
        return damagesSum;
    }
    public void setDamagesSum(Double damagesSum) {
        this.damagesSum = damagesSum;
    }

}
