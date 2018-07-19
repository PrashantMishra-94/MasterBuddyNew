package com.prashant.masterbuddy.ws.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tanmay.agnihotri on 5/19/18.
 */

public class VideoItem {

    @SerializedName("ID")
    private int id;

    @SerializedName("Title")
    private String title;

    @SerializedName("FileName")
    private String filename;

    @SerializedName("UploadedFile")
    private String uploadedFile;

    @SerializedName("Description")
    private String description;

    @SerializedName("UploadedThumbnailImage")
    private String uploadedThumbnailImage;

    @SerializedName("FullThumbnailImagePath")
    private String fullThumbnailImagePath;

    @SerializedName("UserID")
    private int userId;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getFilename() {
        return filename;
    }

    public String getUploadedFile() {
        return uploadedFile;
    }

    public String getDescription() {
        return description;
    }

    public int getUserId() {
        return userId;
    }

    public String getUploadedThumbnailImage() {
        return uploadedThumbnailImage;
    }

    public String getFullThumbnailImagePath() {
        return fullThumbnailImagePath;
    }
}
