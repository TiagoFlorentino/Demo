package com.example.demo.Database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.demo.Dao.GistDB
import com.example.demo.Dao.GistDBDao

@Database(entities = ([GistDB::class]), version = 1)
abstract class GistDatabase : RoomDatabase() {

    abstract fun gistDBDao(): GistDBDao

    companion object {
        private var INSTANCE: GistDatabase? = null

        fun getInstance(context: Context): GistDatabase? {
            if (INSTANCE == null) {
                synchronized(GistDetailsDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        GistDatabase::class.java, "gistDB.db")
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}