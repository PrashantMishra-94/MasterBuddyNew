package com.prashant.masterbuddy.ws.model

import com.google.gson.annotations.SerializedName

/**
 * Created by tanmay.agnihotri on 5/1/18.
 */

class UserRequest(@field:SerializedName("Email")
                  private var email: String?, @field:SerializedName("Password")
                  private var password: String?) {

    fun setEmail(email: String) {
        this.email = email
    }

    fun setPassword(password: String) {
        this.password = password
    }
}
