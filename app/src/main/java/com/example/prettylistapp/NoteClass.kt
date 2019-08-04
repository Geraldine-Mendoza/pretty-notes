package com.example.prettylistapp

import android.util.Log

class Note(setId: String?, t: String, cont: String) {

    val id:String? = setId
    val title = t
    val content = cont

    init {

        Log.d("Note created", "Note class instance has id $id, title of $title, content of $content")
    }

    /*private fun getTitle() {
        return title
    }*/

}