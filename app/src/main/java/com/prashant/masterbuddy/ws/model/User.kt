package com.prashant.masterbuddy.ws.model

import com.google.gson.annotations.SerializedName

/**
 * Created by tanmay.agnihotri on 5/1/18.
 */

class User {

    @SerializedName("UserID")
    val userID: Int? = null
    @SerializedName("Name")
    val name: String? = null
    @SerializedName("Email")
    val email: Any? = null
    @SerializedName("Password")
    val password: Any? = null
    @SerializedName("UserType")
    val userType: Int? = null
    @SerializedName("UserStatus")
    val userStatus: Int? = null
    @SerializedName("RegistrationDate")
    val registrationDate: Any? = null
}
