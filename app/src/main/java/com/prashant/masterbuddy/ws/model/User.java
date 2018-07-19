package com.prashant.masterbuddy.ws.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tanmay.agnihotri on 5/1/18.
 */

public class User {

    @SerializedName("UserID")
    private Integer userID;
    @SerializedName("Name")
    private String name;
    @SerializedName("Email")
    private Object email;
    @SerializedName("Password")
    private Object password;
    @SerializedName("UserType")
    private Integer userType;
    @SerializedName("UserStatus")
    private Integer userStatus;
    @SerializedName("RegistrationDate")
    private Object registrationDate;

    public Integer getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public Object getEmail() {
        return email;
    }

    public Object getPassword() {
        return password;
    }

    public Integer getUserType() {
        return userType;
    }

    public Integer getUserStatus() {
        return userStatus;
    }

    public Object getRegistrationDate() {
        return registrationDate;
    }
}
