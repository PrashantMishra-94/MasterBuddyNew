package com.prashant.masterbuddy.room;

import android.content.Context;

import com.prashant.masterbuddy.VideoList;

import java.util.ArrayList;
import java.util.List;

public class VideoDataSource extends BaseDatasource {

    public VideoDataSource(AppDatabase database, Context context) {
        super(database, context);
    }

    public boolean createVideo(VideoList videoList) {
        if (database.videoDao().getVideo(videoList.getVideoId()) == null) {
            Video video = new Video();
            video.videoId = videoList.getVideoId();
            video.videoTitle = videoList.getVideoTitle();
            video.videoDescription = videoList.getVideoDescription();
            video.videoThumbnail = videoList.getVideoThumbnail();
            database.videoDao().insertUser(video);
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<VideoList> getVideoList() {
        ArrayList<VideoList> videoLists = new ArrayList<>();
        List<Video> videos = database.videoDao().getAll();
        if (videos != null && videos.size() > 0) {
            for (Video video: videos) {
                VideoList list = new VideoList();
                list.setVideoId(video.videoId);
                list.setVideoTitle(video.videoTitle);
                list.setVideoThumbnail(video.videoThumbnail);
                list.setVideoDescription(video.videoDescription);
                videoLists.add(list);
            }
        }
        return videoLists;
    }

    public void deleteVideo(int videoID) {
        database.videoDao().deleteVideo(videoID);
    }
}
