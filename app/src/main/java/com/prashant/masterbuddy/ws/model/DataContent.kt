package com.prashant.masterbuddy.ws.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "data_content")
class DataContent {

    @PrimaryKey(autoGenerate = true)
    var uid: Long? = null

    @ColumnInfo(name = "remote_id")
    var remoteId: Long? = null

    @ColumnInfo(name = "channel_type")
    var channelType: Int? = null
    var contentType: Int? = null
    var contentUrl: String? = null
    var thumbnailUrl: String? = null
    var contentTitle: String? = null

}