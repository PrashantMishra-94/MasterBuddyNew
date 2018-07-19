package com.prashant.masterbuddy.ws.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by tanmay.agnihotri on 5/18/18.
 */

public class SelectVideoResponse {

    @SerializedName("videoList")
    public ArrayList<VideoItem> videoList;

    @SerializedName("totalNoOfVideos")
    public Long totalNoOfVideos;

}
