package com.prashant.masterbuddy.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "video")
public class Video {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "video_id")
    public int videoId;

    @ColumnInfo(name = "video_title")
    public String videoTitle;

    @ColumnInfo(name = "video_description")
    public String videoDescription;

    @ColumnInfo(name = "video_thumbnail")
    public String videoThumbnail;
}
