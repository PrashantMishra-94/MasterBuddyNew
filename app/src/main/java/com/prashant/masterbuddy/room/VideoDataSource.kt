package com.prashant.masterbuddy.room

import android.content.Context

import com.prashant.masterbuddy.VideoList

import java.util.ArrayList

class VideoDataSource(database: AppDatabase, context: Context) : BaseDatasource(database, context) {

    val videoList: ArrayList<VideoList>
        get() {
            val videoLists = ArrayList<VideoList>()
            val videos = database.videoDao().all
            if (videos != null && videos.size > 0) {
                for (video in videos) {
                    val list = VideoList()
                    list.videoId = video.videoId
                    list.videoTitle = video.videoTitle
                    list.videoThumbnail = video.videoThumbnail
                    list.videoDescription = video.videoDescription
                    videoLists.add(list)
                }
            }
            return videoLists
        }

    fun createVideo(videoList: VideoList): Boolean {
        if (database.videoDao().getVideo(videoList.videoId) == null) {
            val video = Video()
            video.videoId = videoList.videoId
            video.videoTitle = videoList.videoTitle
            video.videoDescription = videoList.videoDescription
            video.videoThumbnail = videoList.videoThumbnail
            database.videoDao().insertUser(video)
            return true
        } else {
            return false
        }
    }

    fun deleteVideo(videoID: Int) {
        database.videoDao().deleteVideo(videoID)
    }
}
