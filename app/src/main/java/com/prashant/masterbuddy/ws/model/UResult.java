package com.prashant.masterbuddy.ws.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tanmay.agnihotri on 5/1/18.
 */

public class UResult {

    @SerializedName("messageCode")
    public int messageCode;

    @SerializedName("message")
    public String message;

    @SerializedName("user")
    public User user;

}
