package com.prashant.masterbuddy.room

import android.content.Context
import com.prashant.masterbuddy.ws.model.File

/**
 * Created by prashant.mishra on 26/09/18.
 */
class SavedMediaDataSource(database: AppDatabase, context: Context) : BaseDatasource(database, context) {

    fun insertInSavedMedia(file: File, channel: Int, mediaType: Int, filePath: String?, thumbPath: String?) {
        val media = SavedMedia()
        media.mediaId = file.id!!
        media.channel = channel
        media.mediaType = mediaType
        media.title = file.title
        media.name = file.name
        media.description = file.description
        media.filePath = filePath
        media.thumbnailPath = thumbPath
        database.savedMediaDao().insertMedia(media)
    }

    fun getSavedFiles(mediaType: Int): ArrayList<File> {
        val list = ArrayList<File>()
        for (media in database.savedMediaDao().getAllMedia(mediaType)) {
            val file = File(
                    id = media.mediaId,
                    name = media.name,
                    fileUrl = media.filePath,
                    thumbnailUrl = media.thumbnailPath,
                    title = media.title,
                    description = media.description
            )
            list.add(file)
        }
        return list
    }

    fun isAlreadySaved(mediaId: Int): Boolean {
        return database.savedMediaDao().getSavedMedia(mediaId) != null
    }

    fun deleteSavedFile(mediaId: Int) {
        database.savedMediaDao().deleteSavedMedia(mediaId)
    }
}