package com.example.demo.Objects

import java.io.Serializable

public class Gist: Serializable{

    var id : String = ""
    var created_at : String = ""
    var owner : Owner? = null

}