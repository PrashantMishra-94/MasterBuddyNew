package com.prashant.masterbuddy.ws.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Channels(
        @SerializedName("Id") var id: Int?,
        @SerializedName("Name") var name: String?,
        @SerializedName("MediaList") var mediaList: List<Media>?
) : Parcelable