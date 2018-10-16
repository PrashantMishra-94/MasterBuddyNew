package com.prashant.masterbuddy.ws.model

import com.google.gson.annotations.SerializedName

data class GetAllFilesRequest(
        @SerializedName("Channel") var channel: Int?,
        @SerializedName("Media") var media: Int?,
        @SerializedName("StartIndex") var startIndex: Int
)