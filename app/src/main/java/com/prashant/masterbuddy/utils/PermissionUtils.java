package com.prashant.masterbuddy.utils;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.prashant.masterbuddy.R;

import java.util.ArrayList;

/**
 * Created by tanmay.agnihotri on 1/4/18.
 */

public class PermissionUtils {

    public void requestPermission(Activity activity) {
        ArrayList<String> permissionString = new ArrayList<>();
        try {
            permissionString.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            permissionString.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            permissionString.add(Manifest.permission.INTERNET);
            permissionString.add(Manifest.permission.ACCESS_NETWORK_STATE);
            permissionString.add(Manifest.permission.READ_PHONE_STATE);/*
            permissionString.add(Manifest.permission.ACCESS_NETWORK_STATE);
            permissionString.add(Manifest.permission.LOCATION_HARDWARE);*/

            ActivityCompat.requestPermissions(activity,
                    permissionString.toArray(new String[]{}),
                    1);
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }
    }

    public void checkResults(int[] grantResults, final Activity Activity) {
        if (grantResults.length > 0) {
            for (int grant_result : grantResults) {
                if (grant_result == PackageManager.PERMISSION_DENIED) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Activity);
                    builder.setTitle(R.string.permissions)
                            .setMessage(R.string.permissions_warning)
                            .setPositiveButton(R.string.got_it, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                                    Activity.startActivity(intent);
                                    Activity.finishAffinity();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setCancelable(false)
                            .show();

                    break;
                }
            }
        }

    }

}
