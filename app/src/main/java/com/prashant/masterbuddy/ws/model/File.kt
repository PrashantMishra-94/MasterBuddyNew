package com.prashant.masterbuddy.ws.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class File(
        @SerializedName("Id") var id: Int?,
        @SerializedName("Name") var name: String?,
        @SerializedName("FileUrl") var fileUrl: String?,
        @SerializedName("ThumbnailUrl") var thumbnailUrl: String?,
        @SerializedName("Title") var title: String?,
        @SerializedName("Description") var description: String?
) : Parcelable