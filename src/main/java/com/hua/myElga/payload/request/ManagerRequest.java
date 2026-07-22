package com.hua.myElga.payload.request;

public class ManagerRequest {

    private Long afm;
    private String firstName;
    private String lastName;
    private String jobAddress;

    public ManagerRequest(){}
    public ManagerRequest(Long afm, String firstName, String lastName, String jobAddress) {
        this.afm = afm;
        this.firstName = firstName;
        this.lastName = lastName;
        this.jobAddress = jobAddress;
    }

    public Long getAfm() {
        return afm;
    }
    public void setAfm(Long afm) {
        this.afm = afm;
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

    public String getJobAddress() {
        return jobAddress;
    }
    public void setJobAddress(String jobAddress) {
        this.jobAddress = jobAddress;
    }
}
