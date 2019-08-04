package com.example.prettylistapp

//import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

class NoteInspection : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.note_inspection)

        //set up action bar
        val actionBar = supportActionBar
        //actionBar!!.title =

        actionBar?.setDisplayHomeAsUpEnabled(true)

    }

    //back button return up
    override fun onSupportNavigateUp(): Boolean {

        onBackPressed()
        return true
    }
}
