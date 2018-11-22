package com.prashant.masterbuddy.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.FAIL
import android.arch.persistence.room.Query

/**
 * Created by prashant.mishra on 26/09/18.
 */

@Dao
interface SavedMediaDao {

    @Query("Select * From saved_media where channel = :channel AND media_type = :media")
    fun getAllMedia(channel: Int, media: Int): List<SavedMedia>

    @Insert(onConflict = FAIL)
    fun insertMedia(savedMedia: SavedMedia)

    @Query("Select * From saved_media where media_id = :mediaId AND channel = :channel")
    fun getSavedMedia(mediaId: Int, channel: Int): SavedMedia?

    @Query("Delete from saved_media where media_id = :mediaId")
    fun deleteSavedMedia(mediaId: Int)
}