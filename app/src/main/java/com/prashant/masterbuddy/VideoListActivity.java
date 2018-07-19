package com.prashant.masterbuddy;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.prashant.masterbuddy.room.AppDatabase;

/**
 * Created by tanmay.agnihotri on 4/12/18.
 */

public class VideoListActivity extends AppCompatActivity {

    private Application application;
    private MenuItem menuUpload;
    private MenuItem menuLogin;
    private MenuItem menulogout;
    private VideoListFragment fragment;
    public int listType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (Application)getApplication();
        setContentView(R.layout.video_list_layout);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.ic_action_logo);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionColor)));

        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        fragment = new VideoListFragment();
        ft.replace(R.id.flContainer, fragment);
        ft.commit();
        listType = Constants.LIST_TYPE_HOME;

        BottomNavigationView bottomNavigation = findViewById(R.id.BottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuHome:
                        listType = Constants.LIST_TYPE_HOME;
                        break;

                    case R.id.menuTrending:
                        listType = Constants.LIST_TYPE_TRENDING;
                        break;

                    case R.id.menuPlaylist:
                        listType = Constants.LIST_TYPE_PLAYLIST;
                        break;
                }
                fragment.syncVideoByType();

                return false;
            }
        });
        //bottomNavigation.findViewById(R.id.menuHome).performClick();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.video_list_menu, menu);
        menuUpload = menu.findItem(R.id.menuUpload);
        menuLogin = menu.findItem(R.id.menuLogin);
        menulogout = menu.findItem(R.id.menuLogout);
        setMenuVisibility();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setMenuVisibility();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuLogin:
                Intent intent = new Intent(VideoListActivity.this, MainActivity.class);
                startActivity(intent);
                break;

            case R.id.menuUpload:
                intent = new Intent(VideoListActivity.this, UploadVideoActivity.class);
                startActivity(intent);
                break;

            case R.id.menuLogout:
                SharedPreferences.Editor editor = application.sharedPreferences.edit();
                editor.remove(Constants.USER_ID);
                editor.remove(Constants.USER_TYPE);
                editor.putBoolean(Constants.IS_LOGGED_IN, false);
                editor.apply();
                setMenuVisibility();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setMenuVisibility() {
        if (menuUpload!=null) {
            menuUpload.setVisible(application.sharedPreferences.getInt(Constants.USER_TYPE, 0) == 1);
        }
        if (menuLogin!= null) {
            menuLogin.setVisible(!application.sharedPreferences.getBoolean(Constants.IS_LOGGED_IN, false));
        }
        if (menulogout!= null) {
            menulogout.setVisible(application.sharedPreferences.getBoolean(Constants.IS_LOGGED_IN, false));
        }
    }

    @Override
    protected void onDestroy() {
        AppDatabase.destroyInstance();
        super.onDestroy();
    }
}
