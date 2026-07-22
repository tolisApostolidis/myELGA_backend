package com.hua.myElga.payload.request;

public class PasswordRequest {

    private String password;
    private String email;

    public PasswordRequest(String password, String email){
        this.password = password;
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
