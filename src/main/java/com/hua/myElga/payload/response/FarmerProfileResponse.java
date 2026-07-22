package com.hua.myElga.payload.response;

public class FarmerProfileResponse {
    private String firstName;
    private String lastName;
    private String address;
    private Long id;
    private Long AFM;
    private Long phoneNumber;
    public FarmerProfileResponse(String firstName, String lastName, String address, Long id, Long AFM, Long phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address =address;
        this.id = id;
        this.AFM = AFM;
        this.phoneNumber = phoneNumber;
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

    public Long getId() {
        return id;
    }

    public Long getAFM() {
        return AFM;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }
}
