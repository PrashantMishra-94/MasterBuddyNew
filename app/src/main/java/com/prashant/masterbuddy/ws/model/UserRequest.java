package com.prashant.masterbuddy.ws.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tanmay.agnihotri on 5/1/18.
 */

public class UserRequest {

    @SerializedName("Email")
    private String email;

    @SerializedName("Password")
    private String password;


    public UserRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
