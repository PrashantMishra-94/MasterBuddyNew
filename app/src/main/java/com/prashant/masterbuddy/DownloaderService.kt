package com.prashant.masterbuddy

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.Toast
import com.prashant.masterbuddy.R.string.no
import com.prashant.masterbuddy.utils.Utils
import com.prashant.masterbuddy.ws.model.File
import java.io.BufferedInputStream

/**
 * Created by prashant.mishra on 25/09/18.
 */
/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * TODO: Customize class - update intent actions and extra parameters.
 */
class DownloaderService : IntentService("DownloaderService") {

    override fun onHandleIntent(intent: Intent?) {
        val file = intent!!.getParcelableExtra<File>(File::class.java.name)
        val fileUrl = file?.fileUrl
        val channel = intent.getIntExtra("CHANNEL", Constants.CHANNEL_LEARNING)
        val media = intent.getIntExtra("MEDIA", Constants.MEDIA_VIDEO)
        if (fileUrl != null) {
            val name = fileUrl.substring(fileUrl.lastIndexOf("/")+1)
            val notificationId = file.id!!
            val mNotificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel = NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID,
                        getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT)
                mNotificationManager?.createNotificationChannel(notificationChannel)
            }
            val builder = NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notification)
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                builder.color = resources.getColor(R.color.Green)
            }
            builder.setContentTitle("Downloading $name")
            builder.setOngoing(true)

            builder.setProgress(100, 0, true)
            mNotificationManager?.notify(notificationId, builder.build())

            var isDownloaded: Boolean
            var thumbFile: java.io.File? = null
            var mediaFile: java.io.File? = null
            try {
                thumbFile = downloadThumbnail(file.thumbnailUrl, channel, media)
                val url = Utils.getURL(fileUrl)
                Log.d(TAG, "Parsed URL: $url")
                val connection = url.openConnection()
                connection.connect()
                val totalLength = connection.contentLength
                val buff = BufferedInputStream(url.openStream())
                mediaFile = getFile(fileUrl, channel, media)
                val fos = mediaFile.outputStream()
                buff.use { bis ->
                    fos.use { os ->
                        builder.setProgress(totalLength, 0, totalLength == -1)
                        mNotificationManager?.notify(notificationId, builder.build())
                        var bytesCopied: Long = 0
                        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                        var bytes = bis.read(buffer)
                        while (bytes >= 0) {
                            os.write(buffer, 0, bytes)
                            bytesCopied += bytes
                            bytes = bis.read(buffer)
                            if (totalLength != -1) {
                                builder.setProgress(totalLength, bytesCopied.toInt(), totalLength == -1)
                                mNotificationManager?.notify(notificationId, builder.build())
                            }
                        }
                        os.flush()
                    }
                }
                isDownloaded = true
            } catch (e: Exception) {
                e.printStackTrace()

                isDownloaded = false
            }
            builder.setContentTitle(if (isDownloaded)"$name downloaded" else "Downloading failed")
            builder.setOngoing(false).setProgress(0,0, false)
            mNotificationManager?.notify(notificationId, builder.build())
            if (isDownloaded) {
                insertSaveMedia(file, channel, media, mediaFile?.absolutePath, thumbFile?.absolutePath)
            }
        }
    }

    private fun downloadThumbnail(thumbUrl: String?, channel: Int, mediaType: Int): java.io.File? {
        if (thumbUrl != null) {
            val thumbFile = getFile(thumbUrl, channel, mediaType)
            try {
                val url = Utils.getURL(thumbUrl)
                Log.d(TAG, "Parsed Thumbnail URL: $url")
                val connection = url.openConnection()
                connection.connect()
                val buff = BufferedInputStream(url.openStream())
                val fos = thumbFile.outputStream()
                buff.use { bis ->
                    fos.use { os ->
                        bis.copyTo(os)
                        os.flush()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(TAG, e.toString())
                return null
            }
            return thumbFile
        }
        return null
    }

    private fun insertSaveMedia(file: File, channel: Int, mediaType: Int, filePath: String?, thumbPath: String?) {
        val application = applicationContext as Application
        application.savedMediaDataSource.insertInSavedMedia(file, channel, mediaType, filePath, thumbPath)
    }

    companion object {

        const val TAG = "DownloaderService"

        fun startDownload(context: Context, file: File, channel: Int, mediaType: Int) {
            if ((context.applicationContext as Application).savedMediaDataSource.isAlreadySaved(file.id!!)) {
                Toast.makeText(context, "Already Downloaded", Toast.LENGTH_LONG).show()
            } else {
                val intent = Intent(context, DownloaderService::class.java)
                val bundle = Bundle()
                bundle.putParcelable(File::class.java.name, file)
                bundle.putInt("CHANNEL", channel)
                bundle.putInt("MEDIA", mediaType)
                intent.putExtras(bundle)
                context.startService(intent)
                Toast.makeText(context, "Downloading", Toast.LENGTH_LONG).show()
            }

        }

        private fun getFile(url: String, channel: Int, mediaType: Int, isThumb: Boolean = false): java.io.File {
            val name = url.substring(url.lastIndexOf("/")+1)
            Log.d(TAG, "Doc Name: $name")
            val type = if (isThumb) "Thumbnails" else "Saved"
            val directory = java.io.File(Environment.getExternalStorageDirectory().absolutePath +
                    java.io.File.separator + "MasterBuddy" + java.io.File.separator + type + java.io.File.separator +
                    Utils.getChannelStr(channel) + java.io.File.separator + Utils.getMediaStr(mediaType))
            if (!directory.exists()) {
                directory.mkdirs()
            }
            Log.d(TAG, "Doc Directory: ${directory.absolutePath}")
            return java.io.File(directory.path + java.io.File.separator + name)
        }
    }

}
