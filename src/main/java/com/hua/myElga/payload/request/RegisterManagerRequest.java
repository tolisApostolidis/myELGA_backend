package com.hua.myElga.payload.request;

public class RegisterManagerRequest {

    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Long afm;
    private String jobAddress;

    //// Constructors ////
    public RegisterManagerRequest() {}
    public RegisterManagerRequest(String username, String email, String password, String firstName, String lastName, Long afm, String jobAddress) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.afm = afm;
        this.jobAddress = jobAddress;
    }

    //// Getters - Setters ////

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
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
