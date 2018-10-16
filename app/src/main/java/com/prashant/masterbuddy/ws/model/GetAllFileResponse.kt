package com.prashant.masterbuddy.ws.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GetAllFileResponse(
        @SerializedName("ChannelsList") var channelsList: List<Channels>?,
        @SerializedName("ChannelsCount") var channelsCount: Int?
) : Parcelable {

    fun getFiles(channelType: Int, mediaType: Int): ArrayList<File>? {
        if (channelsList != null && channelsList!!.isNotEmpty()) {
            for (channel in channelsList!!) {
                if (channel.id == channelType) {
                    val mediaList = channel.mediaList
                    if (mediaList != null && mediaList.isNotEmpty()) {
                        for (media in mediaList) {
                            if (media.id == mediaType && media.filesList != null) {
                                return media.filesList!!
                            }
                        }
                    }
                }
            }
        }
        return null
    }

    fun getFilesCount(channelType: Int, mediaType: Int): Int {
        if (channelsList != null && channelsList!!.isNotEmpty()) {
            for (channel in channelsList!!) {
                if (channel.id == channelType) {
                    val mediaList = channel.mediaList
                    if (mediaList != null && mediaList.isNotEmpty()) {
                        for (media in mediaList) {
                            if (media.id == mediaType && media.filesList != null) {
                                return media.filesCount ?: 0
                            }
                        }
                    }
                }
            }
        }
        return 0
    }

}