package com.prashant.masterbuddy.utils

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.util.Log

import com.prashant.masterbuddy.R

import java.util.ArrayList

/**
 * Created by tanmay.agnihotri on 1/4/18.
 */

class PermissionUtils {

    fun requestPermission(activity: Activity) {
        val permissionString = ArrayList<String>()
        try {
            permissionString.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            permissionString.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            permissionString.add(Manifest.permission.INTERNET)
            permissionString.add(Manifest.permission.ACCESS_NETWORK_STATE)
            permissionString.add(Manifest.permission.READ_PHONE_STATE)/*
            permissionString.add(Manifest.permission.ACCESS_NETWORK_STATE);
            permissionString.add(Manifest.permission.LOCATION_HARDWARE);*/

            ActivityCompat.requestPermissions(activity,
                    permissionString.toTypedArray<String>(),
                    1)
        } catch (e: Exception) {
            Log.d("Exception", e.toString())
        }

    }

    fun checkResults(grantResults: IntArray, Activity: Activity) {
        if (grantResults.size > 0) {
            for (grant_result in grantResults) {
                if (grant_result == PackageManager.PERMISSION_DENIED) {
                    val builder = AlertDialog.Builder(Activity)
                    builder.setTitle(R.string.permissions)
                            .setMessage(R.string.permissions_warning)
                            .setPositiveButton(R.string.got_it) { dialog, which ->
                                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                                Activity.startActivity(intent)
                                Activity.finishAffinity()
                            }
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setCancelable(false)
                            .show()

                    break
                }
            }
        }

    }

}
