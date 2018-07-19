package com.prashant.masterbuddy.ws.model

import com.google.gson.annotations.SerializedName

/**
 * Created by tanmay.agnihotri on 5/1/18.
 */

class UResult {

    @SerializedName("messageCode")
    var messageCode: Int = 0

    @SerializedName("message")
    var message: String? = null

    @SerializedName("user")
    var user: User? = null

}
