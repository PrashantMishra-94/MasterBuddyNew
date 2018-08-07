package com.prashant.masterbuddy.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = arrayOf(Video::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun videoDao(): VideoDao

    companion object {

        private var instance: AppDatabase? = null

        fun getAppDatabase(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder<AppDatabase>(context, AppDatabase::class.java, "database")
                        .allowMainThreadQueries().build()
            }
            return instance!!
        }

        fun destroyInstance() {
            instance = null
        }
    }
}
