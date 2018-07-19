package com.prashant.masterbuddy.ws

/**
 * Created by tanmay.agnihotri on 1/5/18.
 */

interface VolleyCallback {

    fun starting()
    fun onSuccess(objectFromJson: Any?)
    fun onSuccess(isSuccess: Boolean)
    fun onFailure()

}
