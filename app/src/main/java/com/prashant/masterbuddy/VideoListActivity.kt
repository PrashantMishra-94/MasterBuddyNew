package com.prashant.masterbuddy

import android.app.FragmentTransaction
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import com.prashant.masterbuddy.room.AppDatabase

/**
 * Created by tanmay.agnihotri on 4/12/18.
 */

class VideoListActivity : AppCompatActivity() {

    private var application: Application? = null
    private var menuUpload: MenuItem? = null
    private var menuLogin: MenuItem? = null
    private var menulogout: MenuItem? = null
    private var fragment: VideoListFragment? = null
    var listType: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        application = getApplication() as Application
        setContentView(R.layout.video_list_layout)
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowHomeEnabled(true)
        actionBar.setLogo(R.drawable.ic_action_logo)
        actionBar.setDisplayUseLogoEnabled(true)
        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.actionColor)))

        val ft = fragmentManager.beginTransaction()
        fragment = VideoListFragment()
        ft.replace(R.id.flContainer, fragment)
        ft.commit()
        listType = Constants.LIST_TYPE_HOME

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.BottomNavigation)
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menuHome -> listType = Constants.LIST_TYPE_HOME

                R.id.menuTrending -> listType = Constants.LIST_TYPE_TRENDING

                R.id.menuPlaylist -> listType = Constants.LIST_TYPE_PLAYLIST
            }
            fragment!!.syncVideoByType()

            false
        }
        //bottomNavigation.findViewById(R.id.menuHome).performClick();
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.video_list_menu, menu)
        menuUpload = menu.findItem(R.id.menuUpload)
        menuLogin = menu.findItem(R.id.menuLogin)
        menulogout = menu.findItem(R.id.menuLogout)
        setMenuVisibility()
        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {
        super.onResume()
        setMenuVisibility()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuLogin -> {
                var intent = Intent(this@VideoListActivity, MainActivity::class.java)
                startActivity(intent)
            }

            R.id.menuUpload -> {
                intent = Intent(this@VideoListActivity, UploadVideoActivity::class.java)
                startActivity(intent)
            }

            R.id.menuLogout -> {
                val editor = application!!.sharedPreferences.edit()
                editor.remove(Constants.USER_ID)
                editor.remove(Constants.USER_TYPE)
                editor.putBoolean(Constants.IS_LOGGED_IN, false)
                editor.apply()
                setMenuVisibility()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setMenuVisibility() {
        if (menuUpload != null) {
            menuUpload!!.isVisible = application!!.sharedPreferences.getInt(Constants.USER_TYPE, 0) == 1
        }
        if (menuLogin != null) {
            menuLogin!!.isVisible = !application!!.sharedPreferences.getBoolean(Constants.IS_LOGGED_IN, false)
        }
        if (menulogout != null) {
            menulogout!!.isVisible = application!!.sharedPreferences.getBoolean(Constants.IS_LOGGED_IN, false)
        }
    }

    override fun onDestroy() {
        AppDatabase.destroyInstance()
        super.onDestroy()
    }
}
