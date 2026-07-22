package com.hua.myElga.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "elga")
public class ElgaManager{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private Long AFM;
    @Column
    private String jobAddress;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_manager_id")
    private User user;

    //// Default Constructor ////
    public ElgaManager(){}

    public ElgaManager(String firstName, String lastName, Long AFM, String jobAddress, User user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.AFM = AFM;
        this.jobAddress = jobAddress;
        this.user = user;
    }

    //// Getters and Setters ////
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getAFM() {
        return AFM;
    }
    public void setAFM(Long AFM) {
        this.AFM = AFM;
    }

    public String getJobAddress() {
        return jobAddress;
    }
    public void setJobAddress(String jobAddress) {
        this.jobAddress = jobAddress;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

}
