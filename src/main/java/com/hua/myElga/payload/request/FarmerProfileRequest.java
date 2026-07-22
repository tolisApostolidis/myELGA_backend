package com.hua.myElga.payload.request;

public class FarmerProfileRequest {

    private String address;
    private Long phoneNumber;

    public FarmerProfileRequest(String address, Long phoneNumber) {
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
    public FarmerProfileRequest(){}

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
}
