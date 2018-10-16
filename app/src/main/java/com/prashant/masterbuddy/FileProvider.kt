package com.prashant.masterbuddy

import android.widget.Toast
import com.prashant.masterbuddy.utils.Utils
import com.prashant.masterbuddy.ws.JsonServices
import com.prashant.masterbuddy.ws.VolleyCallback
import com.prashant.masterbuddy.ws.model.File
import com.prashant.masterbuddy.ws.model.GetAllFileResponse

/**
 * Created by prashant.mishra on 26/09/18.
 */
class FileProvider(private val context: Application) {

    private var response: GetAllFileResponse? = null

    fun init(method: (Boolean)->Unit) {
        if (Utils.isConnectingToInternet(context, true)) {
            val jsonService = JsonServices(context, object : VolleyCallback {
                override fun starting() {
                    Toast.makeText(context, "Please Wait...", Toast.LENGTH_LONG).show()
                }

                override fun onSuccess(objectFromJson: Any?) {
                    response = objectFromJson as? GetAllFileResponse
                    method(response != null)
                }

                override fun onSuccess(isSuccess: Boolean) {}

                override fun onFailure() {
                    Toast.makeText(context, "Unable To connect to server", Toast.LENGTH_LONG).show()
                    method(false)
                }

            })
            jsonService.getAllFiles()
        } else method(false)
    }

    fun getFiles(channel: Int, mediaType: Int): ArrayList<File> {
        return if (channel == Constants.CHANNEL_SAVED) {
            context.savedMediaDataSource.getSavedFiles(mediaType)
        } else {
            response?.getFiles(channelType = channel, mediaType = mediaType) ?: ArrayList()
        }
    }

    fun getFileCount(channel: Int, mediaType: Int): Int {
        return response?.getFilesCount(channel, mediaType) ?: 0
    }

    fun checkForNewFiles(channel: Int, media: Int, startIndex: Int, callback: (Boolean)->Unit) {
        if (Utils.isConnectingToInternet(context, true)) {
            val jsonService = JsonServices(context, object : VolleyCallback {
                override fun starting() {}

                override fun onSuccess(objectFromJson: Any?) {
                    val response = objectFromJson as? GetAllFileResponse
                    val files = response?.getFiles(channel, media)
                    if (files != null && files.isNotEmpty()) {
                        response.getFiles(channel, media)?.addAll(files)
                        callback(true)
                    } else callback(false)
                }

                override fun onSuccess(isSuccess: Boolean) {}

                override fun onFailure() {
                    callback(false)
                }

            })
            jsonService.getAllFiles(channel, media, startIndex)
        }
    }

}