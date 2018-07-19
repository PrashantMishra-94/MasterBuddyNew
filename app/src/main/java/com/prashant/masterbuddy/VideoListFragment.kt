package com.prashant.masterbuddy

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.ProgressBar
import android.widget.Toast

import com.prashant.masterbuddy.ws.JsonServices
import com.prashant.masterbuddy.ws.VolleyCallback
import com.prashant.masterbuddy.ws.model.SelectVideoResponse
import com.prashant.masterbuddy.ws.model.VideoItem

import java.util.ArrayList
import java.util.Locale

/**
 * Created by tanmay.agnihotri on 5/17/18.
 */

class VideoListFragment : android.app.Fragment() {

    private var pbSyncVideo: ProgressBar? = null
    private var videoLists = ArrayList<VideoList>()
    private var videoListAdapter: VideoListAdapter? = null
    private var swipeView: SwipeRefreshLayout? = null
    private var totalVideos: Long = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.video_list_fragment, container, false)

        val rvVideoList = view.findViewById<RecyclerView>(R.id.rvVideoList)
        pbSyncVideo = view.findViewById(R.id.pbSyncVideo)


        syncVideoList(0, (activity as VideoListActivity).listType)
        videoListAdapter = VideoListAdapter(this, videoLists)
        val mLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvVideoList.layoutManager = mLayoutManager
        rvVideoList.itemAnimator = DefaultItemAnimator()
        rvVideoList.addItemDecoration(DividerItemDecoration(activity,
                DividerItemDecoration.VERTICAL))
        rvVideoList.adapter = videoListAdapter
        swipeView = view.findViewById(R.id.swipeRefresh)
        swipeView!!.setOnRefreshListener {
            videoLists.clear()
            syncVideoList(0, (activity as VideoListActivity).listType)
        }

        return view
    }

    fun syncVideoByType() {
        val listType = (activity as VideoListActivity).listType
        if (listType == Constants.LIST_TYPE_PLAYLIST) {
            videoLists = (activity.applicationContext as Application)
                    .mVideoDataSource.videoList
            videoListAdapter!!.refresh(videoLists)
        } else {
            videoLists.clear()
            videoListAdapter!!.notifyDataSetChanged()
            syncVideoList(0, listType)
        }
    }

    private fun syncVideoList(startIndex: Int, listType: Int) {

        val jsonServices = JsonServices(activity, object : VolleyCallback {
            override fun starting() {
                pbSyncVideo!!.visibility = View.VISIBLE
            }

            override fun onSuccess(objectFromJson: Any?) {
                if (activity != null && !activity.isDestroyed) {
                    val selectVideoResponse = objectFromJson as? SelectVideoResponse
                    var videoList: VideoList
                    if (selectVideoResponse != null && selectVideoResponse.videoList!!.size > 0) {
                        totalVideos = selectVideoResponse.totalNoOfVideos!!
                        for (videoItem in selectVideoResponse.videoList!!) {
                            videoList = VideoList()
                            videoList.videoId = videoItem.id
                            videoList.videoTitle = videoItem.filename
                            videoList.videoDescription = videoItem.description
                            videoList.videoThumbnail = videoItem.uploadedThumbnailImage
                            videoLists.add(videoList)
                        }
                        videoListAdapter!!.notifyDataSetChanged()
                    }
                    pbSyncVideo!!.visibility = View.GONE
                    swipeView!!.isRefreshing = false
                }
            }

            override fun onSuccess(isSuccess: Boolean) {}

            override fun onFailure() {
                if (activity != null && !activity.isDestroyed) {
                    pbSyncVideo!!.visibility = View.GONE
                    swipeView!!.isRefreshing = false
                    Toast.makeText(activity, "Unable To connect to server", Toast.LENGTH_SHORT).show()
                }
            }
        })
        jsonServices.getVideoList(startIndex, listType)
    }

    fun addNewVideosInList(index: Int) {
        if ((activity as VideoListActivity).listType == Constants.LIST_TYPE_PLAYLIST) return
        if (index < totalVideos - 1) {
            syncVideoList(index + 1, (activity as VideoListActivity).listType)
        }
    }

    fun addToPlayList(view: View, video: VideoList) {
        val popup = PopupMenu(activity, view)
        popup.menuInflater.inflate(R.menu.menu_popup, popup.menu)
        popup.menu.findItem(R.id.menuPlaylist).isVisible = (activity as VideoListActivity).listType != Constants.LIST_TYPE_PLAYLIST
        popup.menu.findItem(R.id.menuRemovePlaylist).isVisible = (activity as VideoListActivity).listType == Constants.LIST_TYPE_PLAYLIST
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menuPlaylist -> if ((activity.applicationContext as Application)
                                .mVideoDataSource.createVideo(video)) {
                    Toast.makeText(activity, String.format(Locale.ENGLISH, "%s added to playlist",
                            video.videoTitle), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, "Video already exists in playlist", Toast.LENGTH_SHORT).show()
                }
                R.id.menuRemovePlaylist -> {
                    (activity.applicationContext as Application).mVideoDataSource.deleteVideo(video.videoId)
                    syncVideoByType()
                    Toast.makeText(activity, String.format(Locale.ENGLISH, "%s removed from playlist",
                            video.videoTitle), Toast.LENGTH_SHORT).show()
                }
            }
            false
        }
        popup.show()
    }
}
