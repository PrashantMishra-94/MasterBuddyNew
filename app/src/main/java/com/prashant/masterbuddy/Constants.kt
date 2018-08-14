package com.prashant.masterbuddy

/**
 * Created by tanmay.agnihotri on 1/14/18.
 */

interface Constants {
    companion object {


        val USER_ID = "user_id"
        val USER_TYPE = "user_type"
        val IS_LOGGED_IN = "is_logged_in"
        val USERNAME = "username"
        val PASSWORD = "password"
        val USER_EMAIL = "user_email"
        val USER_STATUS = "user_status"

        val MY_SHARED_PREFERENCES = "com.frontier.shared_preferences"

        val NOTIFICATION_CHANNEL_ID = "Move_Inventory_Channel"

        val LIST_TYPE_HOME = 1
        val LIST_TYPE_TRENDING = 2
        val LIST_TYPE_PLAYLIST = 3

        const val CHANNEL_LEARNING = 0
        const val CHANNEL_MUSIC = 0
        const val CHANNEL_TREATMENT = 0
        const val CHANNEL_SHOW = 0
        const val CHANNEL_RECENTS = 0

        const val CONTENT_VIDEO = 0
        const val CONTENT_AUDIO = 1
        const val CONTENT_IMAGE = 2
        const val CONTENT_DOCS = 3
    }
}
