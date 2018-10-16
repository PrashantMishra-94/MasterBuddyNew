package com.prashant.masterbuddy

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import com.prashant.masterbuddy.utils.Utils
import com.prashant.masterbuddy.ws.model.File
import java.io.BufferedInputStream
import java.lang.ref.WeakReference
import java.net.URI
import java.net.URL

/**
 * Created by prashant.mishra on 24/09/18.
 */
class DocumentDownloader private constructor(context: Context, progressBar: ProgressBar, private val url: String,
                                             private val file: java.io.File): AsyncTask<Void, Int, Boolean>() {

    private val weakContext = WeakReference(context)
    private val weakBar = WeakReference(progressBar)

    override fun onPreExecute() {
        val progressBar = weakBar.get()
        if (progressBar != null) {
            progressBar.visibility = View.VISIBLE
        }
    }

    override fun doInBackground(vararg params: Void?): Boolean {
        try {
            Log.d(TAG, "Document URL: $url")
            val url = Utils.getURL(this.url)
            Log.d(TAG, "Parsed URL: $url")
            url.openConnection().connect()
            val bis = BufferedInputStream(url.openStream())
            val fos = file.outputStream()
            bis.use {buff ->
                fos.use {
                    buff.copyTo(out = it)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, e.toString())
            return false
        }
        return true
    }

    override fun onPostExecute(result: Boolean) {
        val context = weakContext.get()
        val bar = weakBar.get()
        if (context != null && bar != null && !(context as Activity).isFinishing) {
            context.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            bar.visibility = View.GONE
            if (result) {
                Utils.openChooserForFile(context = context, file = file)
            } else {
                Toast.makeText(context, "Not able to Download, Please try again.", Toast.LENGTH_LONG).show()
            }
        }

    }

    companion object {
        private val TAG = DocumentDownloader::class.java.name

        fun openDocumentOrDownload(context: Context, docFile: File) {
            val file = getFile(context, file = docFile)
            if (file.exists()) {
                Utils.openChooserForFile(context, file)
            } else {
                if (Utils.isConnectingToInternet(context, true)) {
                    val progressBar = (context as Activity).findViewById<ProgressBar>(R.id.progress_bar)
                    context.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    //val url = "http://masterbuddy.com/Files/Buddy/LearningAssets/Document/QM_Survey_Export_Iota_(Single Format)__20180924_144618.xls"
                    //DocumentDownloader(context, progressBar, url, file).execute()
                    DocumentDownloader(context, progressBar, docFile.fileUrl!!, file).execute()
                }
            }
        }

        private fun getFile(context: Context, file: File): java.io.File {
            val url = file.fileUrl!!
            //val url = "http://masterbuddy.com/Files/Buddy/LearningAssets/Document/QM_Survey_Export_Iota_(Single Format)__20180924_144618.xls"
            val name = url.substring(url.lastIndexOf("/")+1)
            Log.d(TAG, "Doc Name: $name")
            val directory = java.io.File(context.externalCacheDir.absolutePath +
                    java.io.File.separator + file.id!!)
            if (!directory.exists()) {
                directory.mkdirs()
            }
            Log.d(TAG, "Doc Directory: ${directory.absolutePath}")
            return java.io.File(directory.path + java.io.File.separator + name)
        }
    }


}