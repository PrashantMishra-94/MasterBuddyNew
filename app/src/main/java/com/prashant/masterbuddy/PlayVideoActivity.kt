package com.prashant.masterbuddy

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
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
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory

import java.util.Locale

class PlayVideoActivity : AppCompatActivity() {

    private var videoUrl = "http://masterbuddy.com/FileCS.ashx?Id=%d"
    private var progressBar: ProgressBar? = null
    private var playerView: PlayerView? = null
    private var player: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_video)

        val bundle = intent.extras
        if (bundle != null) {
            videoUrl = String.format(Locale.ENGLISH, videoUrl, bundle.getInt("ID"))
        }
        progressBar = findViewById(R.id.progress_bar)
        playerView = findViewById(R.id.player_view)
        uri = Uri.parse(videoUrl)

    }

    private fun initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                DefaultRenderersFactory(this),
                DefaultTrackSelector(), DefaultLoadControl())

        playerView!!.player = player

        player!!.playWhenReady = playWhenReady
        player!!.addListener(EventListener())
        player!!.seekTo(currentWindow, playbackPosition)

        val mediaSource = buildMediaSource()
        player!!.prepare(mediaSource, true, false)
    }

    private fun buildMediaSource(): MediaSource {
        return ExtractorMediaSource.Factory(
                DefaultHttpDataSourceFactory("MasterBuddy")).createMediaSource(uri)
    }

    public override fun onStart() {
        super.onStart()
        if (BuildConfig.VERSION_CODE > 23) {
            initializePlayer()
        }
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUi()
        if (BuildConfig.VERSION_CODE <= 23 || player == null) {
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
        if (BuildConfig.VERSION_CODE <= 23) {
            releasePlayer()
        }
    }

    public override fun onStop() {
        super.onStop()
        if (BuildConfig.VERSION_CODE > 23) {
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

        override fun onTimelineChanged(timeline: Timeline, manifest: Any, reason: Int) {

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
