package com.prashant.masterbuddy.ws.model

import com.google.gson.annotations.SerializedName

/**
 * Created by tanmay.agnihotri on 5/19/18.
 */

class VideoItem {

    @SerializedName("ID")
    val id: Int = 0

    @SerializedName("Title")
    val title: String? = null

    @SerializedName("FileName")
    val filename: String? = null

    @SerializedName("UploadedFile")
    val uploadedFile: String? = null

    @SerializedName("Description")
    val description: String? = null

    @SerializedName("UploadedThumbnailImage")
    val uploadedThumbnailImage: String? = null

    @SerializedName("FullThumbnailImagePath")
    val fullThumbnailImagePath: String? = null

    @SerializedName("UserID")
    val userId: Int = 0
}
