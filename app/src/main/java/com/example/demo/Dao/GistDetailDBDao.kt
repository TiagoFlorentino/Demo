package com.example.demo.Dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface GistDetailDBDao {

    @Query("SELECT * from GistDetailDB")
    fun getAll(): List<GistDetailDB>

    @Query("SELECT * from GistDetailDB WHERE id = :id")
    fun getGist(id : String): List<GistDetailDB>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(gistDetailDB: GistDetailDB)

    @Query("DELETE from GistDetailDB")
    fun deleteAll()
}