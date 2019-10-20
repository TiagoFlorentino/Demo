package com.example.demo.Database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.demo.Dao.GistDetailDB
import com.example.demo.Dao.GistDetailDBDao

@Database(entities = ([GistDetailDB::class]), version = 1)
abstract class GistDetailsDatabase : RoomDatabase() {

    abstract fun gistDetailDBDao(): GistDetailDBDao

    companion object {
        private var INSTANCE: GistDetailsDatabase? = null

        fun getInstance(context: Context): GistDetailsDatabase? {
            if (INSTANCE == null) {
                synchronized(GistDetailsDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        GistDetailsDatabase::class.java, "gistDetailDB.db")
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