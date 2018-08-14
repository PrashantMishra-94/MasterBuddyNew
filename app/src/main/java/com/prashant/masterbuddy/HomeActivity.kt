package com.prashant.masterbuddy

import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), View.OnClickListener {

    var selectedContent = Constants.CONTENT_VIDEO
    var backgroundImage: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_home)
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val drawable = when (checkedId) {
                R.id.radioLearning -> R.drawable.background_learning_image
                R.id.radioMusic -> R.drawable.background_music_image
                R.id.radioShows -> R.drawable.background_shows_image
                R.id.radioTreatment -> R.drawable.background_treatments_image
                R.id.radioRecent -> R.drawable.background_recent_image
                else -> R.drawable.background_learning_image
            }
            Glide.with(this).load(drawable).into(imgHomeBackground)
            //setImageInImageView(imgHomeBackground, drawable)
            setHomeVisibility(true)
        }
        Glide.with(this).load(R.drawable.ic_video_icon).into(imgVideo)
        Glide.with(this).load(R.drawable.ic_audio_icon).into(imgAudio)
        Glide.with(this).load(R.drawable.ic_images_icon).into(imgImages)
        Glide.with(this).load(R.drawable.ic_docs_icon).into(imgDocs)
        Glide.with(this).load(R.drawable.background_learning_image).into(imgHomeBackground)

        btnBack.setOnClickListener { setHomeVisibility(true) }
        imgVideo.setOnClickListener(this)
        imgAudio.setOnClickListener(this)
        imgImages.setOnClickListener(this)
        imgDocs.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        selectedContent = when (v!!.id) {
            R.id.imgVideo -> Constants.CONTENT_VIDEO
            R.id.imgAudio -> Constants.CONTENT_AUDIO
            R.id.imgImages -> Constants.CONTENT_IMAGE
            R.id.imgDocs -> Constants.CONTENT_DOCS
            else -> Constants.CONTENT_VIDEO
        }
        setHomeVisibility(false)
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
}
