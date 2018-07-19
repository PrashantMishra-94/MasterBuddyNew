package com.prashant.masterbuddy.ws.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tanmay.agnihotri on 5/18/18.
 */

public class SelectVideosRequest {

    @SerializedName("startIndex")
    private int startIndex;

    @SerializedName("lastIndex")
    private int lastIndex;

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public void setLastIndex(int lastIndex) {
        this.lastIndex = lastIndex;
    }
}
