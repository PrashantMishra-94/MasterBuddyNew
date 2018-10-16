package com.prashant.masterbuddy.room

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by prashant.mishra on 26/09/18.
 */

@Entity(tableName = "saved_media")
class SavedMedia {

    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0

    @ColumnInfo(name = "media_id")
    var mediaId: Int = 0

    @ColumnInfo(name = "channel")
    var channel: Int = 0

    @ColumnInfo(name = "media_type")
    var mediaType: Int = 0

    @ColumnInfo(name = "title")
    var title: String? = null

    @ColumnInfo(name = "name")
    var name: String? = null

    @ColumnInfo(name = "description")
    var description: String? = null

    @ColumnInfo(name = "filePath")
    var filePath: String? = null

    @ColumnInfo(name = "thumbnailPath")
    var thumbnailPath: String? = null

}