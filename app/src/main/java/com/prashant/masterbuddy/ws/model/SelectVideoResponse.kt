package com.prashant.masterbuddy.ws.model

import com.google.gson.annotations.SerializedName

import java.util.ArrayList

/**
 * Created by tanmay.agnihotri on 5/18/18.
 */

class SelectVideoResponse {

    @SerializedName("videoList")
    var videoList: ArrayList<VideoItem>? = null

    @SerializedName("totalNoOfVideos")
    var totalNoOfVideos: Long? = null

}
