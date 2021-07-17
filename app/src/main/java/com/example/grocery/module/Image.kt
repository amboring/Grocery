package com.example.grocery.module

import java.io.Serializable

data class Image(
    var src:String,
    var title: String
):Serializable{
    companion object{
        val TITLE="imageid"
        val SRC="SOURCE"
    }
}
