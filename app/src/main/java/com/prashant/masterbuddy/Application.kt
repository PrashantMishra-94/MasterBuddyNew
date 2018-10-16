package com.prashant.masterbuddy

import android.content.Context
import android.content.SharedPreferences
import android.support.multidex.MultiDexApplication
import com.prashant.masterbuddy.room.AppDatabase
import com.prashant.masterbuddy.room.SavedMediaDataSource
import com.prashant.masterbuddy.room.VideoDataSource
import com.prashant.masterbuddy.utils.PermissionUtils

/**
 * Created by tanmay.agnihotri on 1/7/18.
 */

class Application : MultiDexApplication() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var permissionUtils: PermissionUtils
    lateinit var mVideoDataSource: VideoDataSource
    lateinit var savedMediaDataSource: SavedMediaDataSource
    lateinit var fileProvider: FileProvider

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences(Constants.MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        permissionUtils = PermissionUtils()
        fileProvider = FileProvider(this)
        val database = AppDatabase.getAppDatabase(this)
        mVideoDataSource = VideoDataSource(database, this)
        savedMediaDataSource = SavedMediaDataSource(database, this)
    }

}

