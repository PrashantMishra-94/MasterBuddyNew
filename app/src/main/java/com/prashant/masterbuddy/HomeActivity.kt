package com.prashant.masterbuddy

import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.prashant.masterbuddy.utils.RecyclerViewItemDecorator
import com.prashant.masterbuddy.ws.model.GetAllFileResponse
import com.prashant.masterbuddy.ws.model.Media
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity(){

    var selectedMedia = Constants.MEDIA_VIDEO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_home)
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val drawable = when (checkedId) {
                R.id.radioLearning -> R.drawable.background_learning_image
                R.id.radioMusic -> R.drawable.background_music_image
                R.id.radioShows -> R.drawable.background_shows_image
                R.id.radioTreatment -> R.drawable.background_treatments_image
                R.id.radioSaved -> R.drawable.background_recent_image
                else -> R.drawable.background_learning_image
            }
            Glide.with(this).load(drawable).apply(RequestOptions().placeholder(copy(imgHomeBackground.drawable))).into(imgHomeBackground)
            setHomeVisibility(true)
        }
        Glide.with(this).load(R.drawable.ic_video_icon).into(imgVideo as ImageView)
        Glide.with(this).load(R.drawable.ic_audio_icon).into(imgAudio as ImageView)
        Glide.with(this).load(R.drawable.ic_images_icon).into(imgImages as ImageView)
        Glide.with(this).load(R.drawable.ic_docs_icon).into(imgDocs as ImageView)
        Glide.with(this).load(R.drawable.background_learning_image).into(imgHomeBackground as ImageView)

        btnBack.setOnClickListener { setHomeVisibility(true) }
        imgVideo.setOnClickListener(clickListener)
        imgAudio.setOnClickListener(clickListener)
        imgImages.setOnClickListener(clickListener)
        imgDocs.setOnClickListener(clickListener)
        rvThumbnails.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvThumbnails.addItemDecoration(RecyclerViewItemDecorator(20, 20, 20, 20))
    }

    private val clickListener = View.OnClickListener { v ->
        selectedMedia = when (v!!.id) {
            R.id.imgVideo -> Constants.MEDIA_VIDEO
            R.id.imgAudio -> Constants.MEDIA_AUDIO
            R.id.imgImages -> Constants.MEDIA_IMAGE
            R.id.imgDocs -> Constants.MEDIA_DOCS
            else -> Constants.MEDIA_VIDEO
        }
        setHomeVisibility(false)
        val fileProvider = (application as Application).fileProvider
        val selectedChannel = getSelectedChannel()
        rvThumbnails.adapter = HomeAdapter(this, selectedChannel, selectedMedia,
                fileProvider.getFiles(selectedChannel, selectedMedia), fileProvider.getFileCount(selectedChannel, selectedMedia))
    }

    private fun getSelectedChannel(): Int {
        return when (radioGroup.checkedRadioButtonId) {
            R.id.radioLearning -> Constants.CHANNEL_LEARNING
            R.id.radioMusic -> Constants.CHANNEL_MUSIC
            R.id.radioShows -> Constants.CHANNEL_SHOW
            R.id.radioTreatment -> Constants.CHANNEL_TREATMENT
            R.id.radioSaved -> Constants.CHANNEL_SAVED
            else -> R.drawable.background_learning_image
        }
    }

    override fun onBackPressed() {
        if (btnBack.visibility == View.VISIBLE) {
            btnBack.performClick()
        } else {
            super.onBackPressed()
        }
    }

    private fun setHomeVisibility(flag: Boolean) {
        btnBack.visibility = if (flag) View.INVISIBLE else View.VISIBLE
        rvThumbnails.visibility = if (flag) View.GONE else View.VISIBLE
        lytContentButtons.visibility = if (flag) View.VISIBLE else View.GONE
    }

    fun checkForNewFiles(channel: Int, media: Int, startIndex: Int) {
        (application as Application).fileProvider.checkForNewFiles(channel, media, startIndex) {
            if (it) {
                val adapter = rvThumbnails.adapter as? HomeAdapter
                if (adapter?.channelType == channel && adapter.mediaType == media) {
                    adapter.isCheckingForNewFiles = false
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun copy(drawable: Drawable): Drawable? { // notice the .asBitmap() on the first load
        var bitmap: Bitmap? = null
        if (drawable is BitmapDrawable) {
            bitmap = drawable.bitmap
        }
        return if (bitmap != null) {
            bitmap = bitmap.copy(bitmap.config, bitmap.isMutable)
            BitmapDrawable(resources, bitmap)
        } else {
            null
        }
    }
}
