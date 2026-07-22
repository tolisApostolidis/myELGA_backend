package com.hua.myElga.payload.response;

public class PhoneNumberResponse {

    private Long phoneNumber;

    public PhoneNumberResponse(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
