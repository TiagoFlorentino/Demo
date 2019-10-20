package com.example.demo.Dao

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull

@Entity(tableName = "GistDetailDB")
data class GistDetailDB(@NonNull @PrimaryKey var id: String,
                        @ColumnInfo(name = "url") var url: String,
                        @ColumnInfo(name = "forks_url") var forks_url: String,
                        @ColumnInfo(name = "node_id") var node_id: String,
                        @ColumnInfo(name = "git_pull_url") var git_pull_url: String,
                        @ColumnInfo(name = "git_push_url") var git_push_url: String,
                        @ColumnInfo(name = "html_url") var html_url: String,
                        @ColumnInfo(name = "created_at") var created_at: String,
                        @ColumnInfo(name = "updated_at") var updated_at: String,
                        @ColumnInfo(name = "comments") var comments: Int,
                        @ColumnInfo(name = "comments_url") var comments_url: String,
                        @ColumnInfo(name = "description") var description: String,
                        //Owner
                        @ColumnInfo(name = "owner_id") var owner_id: Int,
                        @ColumnInfo(name = "owner_avatar_url") var owner_avatar_url: String,
                        @ColumnInfo(name = "owner_login") var owner_login: String,
                        @ColumnInfo(name = "owner_node_id") var owner_node_id: String,
                        @ColumnInfo(name = "owner_html_url") var owner_html_url: String,
                        @ColumnInfo(name = "owner_type") var owner_type: String,
                        @ColumnInfo(name = "owner_site_admin") var owner_site_admin: Boolean
                        ){

    constructor() : this("","","","","","","","","",0,"","",0,"","","","","",false)
}
