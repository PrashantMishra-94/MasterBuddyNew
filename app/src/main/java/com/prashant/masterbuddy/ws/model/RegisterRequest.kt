package com.prashant.masterbuddy.ws.model

import com.google.gson.annotations.SerializedName

/**
 * Created by tanmay.agnihotri on 5/1/18.
 */

class RegisterRequest(@field:SerializedName("Name")
                      private var name: String?, @field:SerializedName("Email")
                      private var EmailId: String?, @field:SerializedName("Password")
                      private var password: String?, @field:SerializedName("RegistrationDate")
                      private var RegistrationDate: String?) {

    fun setName(name: String) {
        this.name = name
    }

    fun setEmailId(emailId: String) {
        EmailId = emailId
    }

    fun setPassword(password: String) {
        this.password = password
    }

    fun setRegistrationDate(registrationDate: String) {
        RegistrationDate = registrationDate
    }
}
