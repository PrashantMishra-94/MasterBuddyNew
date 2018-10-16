package com.prashant.masterbuddy

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.bumptech.glide.Glide
import com.prashant.masterbuddy.utils.NewPermissionUtils
import com.prashant.masterbuddy.utils.Utils
import com.prashant.masterbuddy.ws.JsonServices
import com.prashant.masterbuddy.ws.VolleyCallback
import com.prashant.masterbuddy.ws.model.GetAllFileResponse
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    var permissionUtils: NewPermissionUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)
        Glide.with(this).load(R.drawable.splash_screen).into(imgSplash)
        permissionUtils = NewPermissionUtils(this) {init()}
        permissionUtils!!.getPermissions()
    }

    private fun init() {
        (application as Application).fileProvider.init {
            if ((application as Application).sharedPreferences.getBoolean(Constants.IS_LOGGED_IN, false)) {
                if (!it) {
                    Toast.makeText(this@SplashActivity, "Unable To connect to server", Toast.LENGTH_LONG).show()
                }
                startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
            } else {
                startActivity(Intent(this, MainActivity::class.java))

            }
            finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionUtils?.onRequestPermissionsResult()
    }

}
