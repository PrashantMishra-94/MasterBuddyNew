package com.prashant.masterbuddy.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast

import com.prashant.masterbuddy.R

import org.apache.commons.io.IOUtils

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Random
import java.util.UUID
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by tanmay.agnihotri on 1/1/18.
 */

object Utils {

    val currentDate: String
        get() = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Calendar.getInstance().time)

    val currentTime: String
        get() = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(Calendar.getInstance().time)

    fun isEmailValid(email: String): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetworkInfo: NetworkInfo? = null
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.activeNetworkInfo
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun hideKeyboard(context: Context) {
        val view = (context as AppCompatActivity).currentFocus
        if (view != null) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun saveImage(context: Context, bitmap: Bitmap): String {
        val root = Environment.getExternalStorageDirectory().toString() + "/" + context.getString(R.string.app_name) + "/Images/"
        val myDir = File(root)
        myDir.mkdirs()
        val generator = Random()
        var n = 10000
        n = generator.nextInt(n)
        val fname = "Image-$n.jpg"
        val file = File(myDir, fname)
        Log.i("TAG", "" + file)
        if (file.exists())
            file.delete()
        try {
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return file.absolutePath
    }

    fun getDeviceId(context: Context): String {
        val tm = (context as AppCompatActivity).baseContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        val tmDevice: String
        val tmSerial: String
        val androidId: String

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return TODO;
        }
        tmDevice = "" + tm?.deviceId
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return TODO;
        }
        tmSerial = "" + tm.simSerialNumber
        androidId = "" + android.provider.Settings.Secure.getString(context.contentResolver, android.provider.Settings.Secure.ANDROID_ID)

        val deviceUuid = UUID(androidId.hashCode().toLong(), tmDevice.hashCode().toLong() shl 32 or tmSerial.hashCode().toLong())
        return deviceUuid.toString()
    }

    fun convertFileToByteArray(selectedPath: Uri, context: Context): ByteArray {
        var buf = ByteArray(0)
        try {
            val `in` = context.contentResolver.openInputStream(selectedPath) as FileInputStream
            assert(`in` != null)
            buf = IOUtils.toByteArray(`in`)
            `in`.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return buf
    }

    fun isEmpty(edtName: EditText?): Boolean {
        return edtName == null || edtName.text == null || isEmpty(edtName.text.toString())
    }

    fun isEmpty(str: String): Boolean {
        return TextUtils.isEmpty(str) || str.trim { it <= ' ' }.length == 0
    }

    fun isConnectingToInternet(context: Context, showToast: Boolean): Boolean {
        val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (connectivity != null) {
            val info = connectivity.allNetworkInfo
            if (info != null) {
                for (anInfo in info) {
                    if (anInfo.state == NetworkInfo.State.CONNECTED) {
                        return true
                    }
                }
            }
        }
        if (showToast) {
            Toast.makeText(context, "Internet connection not available",
                    Toast.LENGTH_SHORT).show()
        }
        return false
    }
}
