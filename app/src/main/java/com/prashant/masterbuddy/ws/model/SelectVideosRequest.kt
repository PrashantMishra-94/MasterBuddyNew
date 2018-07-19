package com.prashant.masterbuddy.ws.model

import com.google.gson.annotations.SerializedName

/**
 * Created by tanmay.agnihotri on 5/18/18.
 */

class SelectVideosRequest {

    @SerializedName("startIndex")
    private var startIndex: Int = 0

    @SerializedName("lastIndex")
    private var lastIndex: Int = 0

    fun setStartIndex(startIndex: Int) {
        this.startIndex = startIndex
    }

    fun setLastIndex(lastIndex: Int) {
        this.lastIndex = lastIndex
    }
}
