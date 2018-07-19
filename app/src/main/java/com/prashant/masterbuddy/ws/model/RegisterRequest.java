package com.prashant.masterbuddy.ws.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tanmay.agnihotri on 5/1/18.
 */

public class RegisterRequest {

    @SerializedName("Name")
    private String name;

    @SerializedName("Email")
    private String EmailId;

    @SerializedName("Password")
    private String password;

    @SerializedName("RegistrationDate")
    private String RegistrationDate;

    public RegisterRequest(String name, String emailId, String password, String registrationDate) {
        this.name = name;
        EmailId = emailId;
        this.password = password;
        RegistrationDate = registrationDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRegistrationDate(String registrationDate) {
        RegistrationDate = registrationDate;
    }
}
