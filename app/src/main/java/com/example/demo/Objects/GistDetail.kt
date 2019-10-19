package com.example.demo.Objects

import java.io.Serializable

public class GistDetail: Serializable {

    var url : String = ""
    var forks_url : String = ""
    var node_id : String = ""
    var id : String = ""
    var git_pull_url : String = ""
    var git_push_url : String = ""
    var html_url : String = ""
    var public : Boolean = false
    var created_at : String = ""
    var updated_at : String = ""
    var description : String = ""
    var comments : Int = 0
    var comments_url : String = ""
    var owner : Owner? = null



}