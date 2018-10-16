package com.prashant.masterbuddy

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ProgressBar

import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.prashant.masterbuddy.utils.Utils
import java.io.File

import java.util.Locale

class PlayVideoActivity : AppCompatActivity() {

    private val TAG = PlayVideoActivity::class.java.name
    private var videoUrl = ""
    private var progressBar: ProgressBar? = null
    private var playerView: PlayerView? = null
    private var player: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private var uri: Uri? = null
    private var channel = Constants.CHANNEL_LEARNING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_play_video)

        val bundle = intent.extras
        if (bundle != null) {
            videoUrl = bundle.getString("URL")
            channel = bundle.getInt("CHANNEL")
        }
        Log.d(TAG, "URL: $videoUrl")
        progressBar = findViewById(R.id.progress_bar)
        playerView = findViewById(R.id.player_view)
        uri = if (channel == Constants.CHANNEL_SAVED) {
            val file = File(videoUrl)
            Uri.fromFile(file)
        } else {
            Uri.parse(Utils.getURL(videoUrl).toString())
        }
        Log.d(TAG, "Parsed URL: $uri")

    }

    private fun initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                DefaultRenderersFactory(this),
                DefaultTrackSelector(), DefaultLoadControl())

        playerView!!.player = player

        player!!.playWhenReady = playWhenReady
        player!!.addListener(EventListener())
        player!!.seekTo(currentWindow, playbackPosition)

        val mediaSource = if (channel == Constants.CHANNEL_SAVED) buildSavedMediaSource() else buildMediaSource()
        player!!.prepare(mediaSource, true, false)
    }

    /*private fun buildMediaSource(): MediaSource {
        return ExtractorMediaSource.Factory(
                DefaultHttpDataSourceFactory("MasterBuddy")).createMediaSource(uri)
    }*/

    private fun buildMediaSource(): MediaSource {

        val userAgent = "MasterBuddy"

        return if (uri!!.lastPathSegment.contains("mp3") || uri!!.lastPathSegment.contains("mp4")) {
            ExtractorMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent))
                    .createMediaSource(uri)
        } else if (uri!!.lastPathSegment.contains("m3u8")) {
            HlsMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent))
                    .createMediaSource(uri)
        } else {
            val dashChunkSourceFactory = DefaultDashChunkSource.Factory(
                    DefaultHttpDataSourceFactory("ua", DefaultBandwidthMeter()))
            val manifestDataSourceFactory = DefaultHttpDataSourceFactory(userAgent)
            DashMediaSource.Factory(dashChunkSourceFactory, manifestDataSourceFactory).createMediaSource(uri)
        }
    }

    private fun buildSavedMediaSource(): MediaSource {
        val bandwidthMeter = DefaultBandwidthMeter()
        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
        val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)

        val defaultBandwidthMeter = DefaultBandwidthMeter()
        val dataSourceFactory = DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "MasterBuddy"), defaultBandwidthMeter)
        return ExtractorMediaSource.Factory(dataSourceFactory).
                createMediaSource(uri)
    }

    public override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT > 23) {
            initializePlayer()
        }
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUi()
        if (Build.VERSION.SDK_INT <= 23 || player == null) {
            initializePlayer()
        }
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        playerView!!.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    public override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    public override fun onStop() {
        super.onStop()
        if (Build.VERSION.SDK_INT > 23) {
            releasePlayer()
        }
    }

    private fun releasePlayer() {
        if (player != null) {
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentWindowIndex
            playWhenReady = player!!.playWhenReady
            player!!.release()
            player = null
        }
    }

    private inner class EventListener : Player.EventListener {

        override fun onTimelineChanged(timeline: Timeline, manifest: Any?, reason: Int) {

        }

        override fun onTracksChanged(trackGroups: TrackGroupArray, trackSelections: TrackSelectionArray) {

        }

        override fun onLoadingChanged(isLoading: Boolean) {

        }

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            progressBar!!.visibility = if (playbackState == Player.STATE_BUFFERING) View.VISIBLE else View.GONE
        }

        override fun onRepeatModeChanged(repeatMode: Int) {

        }

        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {

        }

        override fun onPlayerError(error: ExoPlaybackException) {

        }

        override fun onPositionDiscontinuity(reason: Int) {

        }

        override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {

        }

        override fun onSeekProcessed() {

        }
    }
}
