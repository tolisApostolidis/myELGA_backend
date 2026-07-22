package com.hua.myElga.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "farmer")
public class Farmer {

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
    private String address;
    @Column
    private Long phoneNumber;

    @OneToMany(mappedBy = "farmer", cascade = CascadeType.ALL)
    private List<Submission> submissions;

    @OneToMany(mappedBy = "farmer", cascade = CascadeType.ALL)
    private List<BreederSubmission> breederSubmissions;

    public List<BreederSubmission> getBreederSubmissions() {
        return breederSubmissions;
    }
    public void setBreederSubmissions(List<BreederSubmission> breederSubmissions) {
        this.breederSubmissions = breederSubmissions;
    }
    public void addBreederSubmission(BreederSubmission breederSubmission) {
        this.breederSubmissions.add(breederSubmission);
    }

    public List<Submission> getSubmissions() {
        return submissions;
    }
    public void setSubmissions(List<Submission> submissions) {
        this.submissions = submissions;
    }
    public void addSubmission(Submission submission) {
        this.submissions.add(submission);
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_farmer_id")
    private User user;

    public Farmer() {}
    public Farmer(String firstName, String lastName, Long AFM, String address, Long phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.AFM = AFM;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public Farmer(String firstName, String lastName, Long AFM, String address, Long phoneNumber, User user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.AFM = AFM;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.user = user;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
