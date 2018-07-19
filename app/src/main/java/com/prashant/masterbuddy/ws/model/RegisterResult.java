package com.prashant.masterbuddy.ws.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tanmay.agnihotri on 5/1/18.
 */

public class RegisterResult {

    @SerializedName("response")
    public Response response;

    public static class Response{
        @SerializedName("message")
        private String message;

        @SerializedName("isSuccessfullyRegistred")
        private boolean isSuccessfullyRegistred;

        @SerializedName("userID")
        private int userID;

        public String getMessage() {
            return message;
        }

        public boolean isSuccessfullyRegistred() {
            return isSuccessfullyRegistred;
        }

        public int isUserID() {
            return userID;
        }
    }
}
