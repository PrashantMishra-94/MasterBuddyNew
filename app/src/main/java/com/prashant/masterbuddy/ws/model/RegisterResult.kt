package com.prashant.masterbuddy.ws.model

import com.google.gson.annotations.SerializedName

/**
 * Created by tanmay.agnihotri on 5/1/18.
 */

class RegisterResult {

    @SerializedName("response")
    var response: Response? = null

    class Response {
        @SerializedName("message")
        val message: String? = null

        @SerializedName("isSuccessfullyRegistred")
        val isSuccessfullyRegistred: Boolean = false

        @SerializedName("userID")
        val isUserID: Int = 0
    }
}
