package com.prashant.masterbuddy

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

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
            lytHome.setBackgroundResource(drawable)
        }
    }
}
