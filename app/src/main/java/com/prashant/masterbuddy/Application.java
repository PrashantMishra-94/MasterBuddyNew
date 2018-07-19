package com.prashant.masterbuddy;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDexApplication;

import com.prashant.masterbuddy.room.AppDatabase;
import com.prashant.masterbuddy.room.VideoDataSource;
import com.prashant.masterbuddy.utils.PermissionUtils;

/**
 * Created by tanmay.agnihotri on 1/7/18.
 */

public class Application extends MultiDexApplication {

    public SharedPreferences sharedPreferences;
    public PermissionUtils permissionUtils;
    public VideoDataSource mVideoDataSource;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences(Constants.MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        permissionUtils = new PermissionUtils();
        createDataBase();
    }



    private void createDataBase() {
        AppDatabase database = AppDatabase.getAppDatabase(this);
        mVideoDataSource = new VideoDataSource(database, this);
    }

}

