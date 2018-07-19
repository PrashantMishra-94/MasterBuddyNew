package com.prashant.masterbuddy

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v4.app.NotificationCompat

import com.prashant.masterbuddy.ws.JsonServices
import com.prashant.masterbuddy.ws.VolleyCallback

import java.util.Random


/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 *
 *
 */
class UploadVideoService : IntentService("UploadVideoService") {

    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            val path = intent.data
            val title = intent.getStringExtra(TITLE)
            val desc = intent.getStringExtra(DESCRIPTION)
            val userId = intent.getIntExtra(USER_ID, 0)
            handleActionImageUpload(path, title, desc, userId)
        }
    }

    private fun handleActionImageUpload(path: Uri, title: String, desc: String, userId: Int) {
        val notificationId = Random().nextInt()
        val mNotificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID,
                    getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT)
            mNotificationManager?.createNotificationChannel(channel)
        }
        val builder = NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            builder.color = resources.getColor(R.color.Green)
        }
        builder.setContentTitle("Uploading $title")
        builder.setOngoing(true)

        val services = JsonServices(this, object : VolleyCallback {
            override fun starting() {
                mNotificationManager?.notify(notificationId, builder.build())
            }

            override fun onSuccess(objectFromJson: Any?) {
                if (mNotificationManager != null) {
                    builder.setContentTitle("$title uploaded")
                    builder.setOngoing(false)
                    mNotificationManager.notify(notificationId, builder.build())
                }
            }

            override fun onSuccess(isSuccess: Boolean) {
                if (mNotificationManager != null) {
                    builder.setContentTitle("$title uploaded")
                    builder.setOngoing(false)
                    mNotificationManager.notify(notificationId, builder.build())
                }
            }

            override fun onFailure() {
                if (mNotificationManager != null) {
                    builder.setContentTitle("Uploading failed")
                    builder.setOngoing(false)
                    mNotificationManager.notify(notificationId, builder.build())
                }
            }
        })
        services.executeVideoRequest(path, title, desc, userId)
    }

    companion object {

        private val USER_ID = "user_id"
        private val TITLE = "title"
        private val DESCRIPTION = "description"

        fun startServiceUpload(ctx: Context, path: Uri, title: String, description: String, userId: Int) {
            val intent = Intent(ctx, UploadVideoService::class.java)
            intent.data = path
            intent.putExtra(TITLE, title)
            intent.putExtra(DESCRIPTION, description)
            intent.putExtra(USER_ID, userId)
            ctx.startService(intent)
        }
    }
}
