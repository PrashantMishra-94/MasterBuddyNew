package com.prashant.masterbuddy.ws.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Media(
        @SerializedName("Id") var id: Int?,
        @SerializedName("Name") var name: String?,
        @SerializedName("FilesCount") var filesCount: Int?,
        @SerializedName("FilesList") var filesList: ArrayList<File>?
) : Parcelable