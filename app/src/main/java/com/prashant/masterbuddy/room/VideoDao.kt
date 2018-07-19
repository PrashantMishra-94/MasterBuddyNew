package com.prashant.masterbuddy.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

import android.arch.persistence.room.OnConflictStrategy.FAIL

@Dao
interface VideoDao {

    @get:Query("Select * FROM video")
    val all: List<Video>

    @Query("Select * From video where video_id = :id")
    fun getVideo(id: Int): Video

    @Insert(onConflict = FAIL)
    fun insertUser(video: Video)

    @Query("Delete from video where video_id = :id")
    fun deleteVideo(id: Int)

}
