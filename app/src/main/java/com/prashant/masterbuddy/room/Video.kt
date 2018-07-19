package com.prashant.masterbuddy.room

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "video")
class Video {

    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0

    @ColumnInfo(name = "video_id")
    var videoId: Int = 0

    @ColumnInfo(name = "video_title")
    var videoTitle: String? = null

    @ColumnInfo(name = "video_description")
    var videoDescription: String? = null

    @ColumnInfo(name = "video_thumbnail")
    var videoThumbnail: String? = null
}
