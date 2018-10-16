package com.prashant.masterbuddy.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import com.prashant.masterbuddy.R

/**
 * Created by prashant.mishra on 25/09/18.
 */
class NewPermissionUtils(private val context: Context, private val method: ()->Unit) {

    fun getPermissions() {
        val OS_Version = Build.VERSION.SDK_INT
        if (OS_Version >= Build.VERSION_CODES.M) {
            when (allPermissionsStatus()) {
                PackageManager.PERMISSION_GRANTED -> method()
                PackageManager.PERMISSION_DENIED -> requestAllPermissions()
            }
        } else {
            method()
        }
    }

    fun onRequestPermissionsResult() {
        when (allPermissionsStatus()) {
            PackageManager.PERMISSION_GRANTED -> method()
            PackageManager.PERMISSION_DENIED -> permissionErrorDialogue()
        }
    }

    private fun permissionErrorDialogue() {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setTitle(R.string.permissions)
        builder.setMessage(R.string.permissions_warning)
        builder.setPositiveButton(R.string.got_it) { dialog, which ->
            dialog.dismiss()
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            context.startActivity(intent)
            (context as Activity).finishAffinity()
        }
        builder.create().show()

    }

    private fun storagePermissionStatus(): Int {
        return ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    /*private fun locationPermissionStatus(): Int {
        return ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun accountPermissionStatus(): Int {
        return ContextCompat.checkSelfPermission(context,
                Manifest.permission.GET_ACCOUNTS)
    }*/

    private fun requestAllPermissions() {

        ActivityCompat.requestPermissions(context as Activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.GET_ACCOUNTS),
                PERMISSION_REQUEST_CODE)
    }

    private fun allPermissionsStatus(): Int {
        if (storagePermissionStatus() == PackageManager.PERMISSION_GRANTED
                /*&& locationPermissionStatus() == PackageManager.PERMISSION_GRANTED
                && accountPermissionStatus() == PackageManager.PERMISSION_GRANTED*/) {
            return PackageManager.PERMISSION_GRANTED
        } else if (storagePermissionStatus() == PackageManager.PERMISSION_DENIED
                /*|| locationPermissionStatus() == PackageManager.PERMISSION_DENIED
                || accountPermissionStatus() == PackageManager.PERMISSION_DENIED*/) {
            return PackageManager.PERMISSION_DENIED
        }
        return 0
    }

    companion object {
        const val PERMISSION_REQUEST_CODE = 100
    }

}