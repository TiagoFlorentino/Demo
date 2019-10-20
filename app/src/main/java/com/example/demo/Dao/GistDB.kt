package com.example.demo.Dao

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull

@Entity(tableName = "GistDB")
data class GistDB(@NonNull @PrimaryKey var id: String,
                        @ColumnInfo(name = "created_at") var created_at: String,
                        //Owner
                        @ColumnInfo(name = "owner_id") var owner_id: Int,
                        @ColumnInfo(name = "owner_avatar_url") var owner_avatar_url: String,
                        @ColumnInfo(name = "owner_login") var owner_login: String,
                        @ColumnInfo(name = "owner_node_id") var owner_node_id: String,
                        @ColumnInfo(name = "owner_html_url") var owner_html_url: String,
                        @ColumnInfo(name = "owner_type") var owner_type: String,
                        @ColumnInfo(name = "owner_site_admin") var owner_site_admin: Boolean){

    constructor() : this("","",0,"","","","","",false)
}