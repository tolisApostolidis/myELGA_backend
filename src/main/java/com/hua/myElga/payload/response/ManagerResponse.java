package com.hua.myElga.payload.response;

import org.apache.catalina.Manager;

public class ManagerResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private Long afm;
    private String jobAddress;

    public ManagerResponse(){}
    public ManagerResponse(Long id, String firstName, String lastName, Long afm, String jobAddress) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.afm = afm;
        this.jobAddress = jobAddress;
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

    public Long getAfm() {
        return afm;
    }

    public void setAfm(Long afm) {
        this.afm = afm;
    }

    public String getJobAddress() {
        return jobAddress;
    }

    public void setJobAddress(String jobAddress) {
        this.jobAddress = jobAddress;
    }
}
