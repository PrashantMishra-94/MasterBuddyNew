package com.prashant.masterbuddy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.prashant.masterbuddy.ws.JsonServices;
import com.prashant.masterbuddy.ws.VolleyCallback;
import com.prashant.masterbuddy.ws.model.SelectVideoResponse;
import com.prashant.masterbuddy.ws.model.VideoItem;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by tanmay.agnihotri on 5/17/18.
 */

public class VideoListFragment extends android.app.Fragment {

    private ProgressBar pbSyncVideo;
    private ArrayList<VideoList> videoLists = new ArrayList<>();
    private VideoListAdapter videoListAdapter;
    private SwipeRefreshLayout swipeView;
    private long totalVideos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_list_fragment, container, false);

        RecyclerView rvVideoList = view.findViewById(R.id.rvVideoList);
        pbSyncVideo = view.findViewById(R.id.pbSyncVideo);


        syncVideoList(0, ((VideoListActivity) getActivity()).listType);
        videoListAdapter = new VideoListAdapter(this, videoLists);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvVideoList.setLayoutManager(mLayoutManager);
        rvVideoList.setItemAnimator(new DefaultItemAnimator());
        rvVideoList.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        rvVideoList.setAdapter(videoListAdapter);
        swipeView = view.findViewById(R.id.swipeRefresh);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                videoLists.clear();
                syncVideoList(0, ((VideoListActivity) getActivity()).listType);
            }
        });

        return view;
    }

    public void syncVideoByType() {
        int listType = ((VideoListActivity) getActivity()).listType;
        if (listType == Constants.LIST_TYPE_PLAYLIST) {
            videoLists = ((Application) getActivity().getApplicationContext())
                    .mVideoDataSource.getVideoList();
            videoListAdapter.refresh(videoLists);
        } else {
            videoLists.clear();
            videoListAdapter.notifyDataSetChanged();
            syncVideoList(0, listType);
        }
    }

    private void syncVideoList(int startIndex, int listType){

        JsonServices jsonServices = new JsonServices(getActivity(), new VolleyCallback() {
            @Override
            public void starting() {
                pbSyncVideo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(Object objectFromJson) {
                if (getActivity() != null && !getActivity().isDestroyed()) {
                    SelectVideoResponse selectVideoResponse = (SelectVideoResponse) objectFromJson;
                    VideoList videoList;
                    totalVideos = selectVideoResponse.totalNoOfVideos;
                    if (selectVideoResponse != null && selectVideoResponse.videoList.size() > 0) {
                        for (VideoItem videoItem : selectVideoResponse.videoList) {
                            videoList = new VideoList();
                            videoList.setVideoId(videoItem.getId());
                            videoList.setVideoTitle(videoItem.getFilename());
                            videoList.setVideoDescription(videoItem.getDescription());
                            videoList.setVideoThumbnail(videoItem.getUploadedThumbnailImage());
                            videoLists.add(videoList);
                        }
                        videoListAdapter.notifyDataSetChanged();
                    }
                    pbSyncVideo.setVisibility(View.GONE);
                    swipeView.setRefreshing(false);
                }
            }

            @Override
            public void onSuccess(boolean isSuccess) {
            }

            @Override
            public void onFailure() {
                if (getActivity() !=null && !getActivity().isDestroyed()) {
                    pbSyncVideo.setVisibility(View.GONE);
                    swipeView.setRefreshing(false);
                    Toast.makeText(getActivity(), "Unable To connect to server", Toast.LENGTH_SHORT).show();
                }
            }
        });
        jsonServices.getVideoList(startIndex, listType);
    }

    public void addNewVideosInList(int index) {
        if (((VideoListActivity) getActivity()).listType == Constants.LIST_TYPE_PLAYLIST) return;
        if (index < totalVideos - 1) {
            syncVideoList(index+1, ((VideoListActivity) getActivity()).listType);
        }
    }

    public void addToPlayList(View view, VideoList video) {
        PopupMenu popup = new PopupMenu(getActivity(), view);
        popup.getMenuInflater().inflate(R.menu.menu_popup, popup.getMenu());
        popup.getMenu().findItem(R.id.menuPlaylist).setVisible(
                ((VideoListActivity) getActivity()).listType != Constants.LIST_TYPE_PLAYLIST);
        popup.getMenu().findItem(R.id.menuRemovePlaylist).setVisible(
                ((VideoListActivity) getActivity()).listType == Constants.LIST_TYPE_PLAYLIST);
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menuPlaylist:
                    if(((Application) getActivity().getApplicationContext())
                            .mVideoDataSource.createVideo(video)) {
                        Toast.makeText(getActivity(), String.format(Locale.ENGLISH, "%s added to playlist",
                                video.getVideoTitle()), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Video already exists in playlist", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.menuRemovePlaylist:
                    ((Application) getActivity().getApplicationContext()).
                            mVideoDataSource.deleteVideo(video.getVideoId());
                    syncVideoByType();
                    Toast.makeText(getActivity(), String.format(Locale.ENGLISH, "%s removed from playlist",
                            video.getVideoTitle()), Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        });
        popup.show();
    }
}
