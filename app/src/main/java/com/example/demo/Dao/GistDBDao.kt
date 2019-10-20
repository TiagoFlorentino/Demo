package com.example.demo.Dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface GistDBDao {

    @Query("SELECT * from GistDB")
    fun getAll(): List<GistDB>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(gistDB: GistDB)

}