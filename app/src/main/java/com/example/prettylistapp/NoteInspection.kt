package com.example.prettylistapp

//import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.note_inspection.*

class NoteInspection : AppCompatActivity() {

    private lateinit var noteTitle: EditText
    private lateinit var noteContent: EditText
    private var listFilesAddress = Note.getListFiles()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.note_inspection)
        setSupportActionBar(inspectionToolbar)

        //set up action bar
        val actionBar = supportActionBar
        actionBar?.title = "Edit Note" // ~ extract?

        //set up back button
        actionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp)

        setUpUIElements()
        setUpInspection()

    }

    //set up menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_inspection, menu)
        return true
    }

    //back button return up
    override fun onSupportNavigateUp(): Boolean {

        saveUpdatedNote()

        onBackPressed()
        return true
    }

    private fun setUpInspection() {

        val notePosition = intent.getIntExtra("noteArrayPosition", 0)

        // if position not within size of array, there was an error!
        if ( notePosition !in (0..listFilesAddress.size) ) {
            Log.d("note inspection", "the position is outside of the possible range! position is $notePosition")
            errorToastAlert("requesting note information failed.", noteTitle.context)
            onBackPressed()
        }
        val noteItemSelected = listFilesAddress[notePosition]

        noteTitle.setText(noteItemSelected.title)
        noteContent.setText(noteItemSelected.content)
    }

    private fun setUpUIElements() {
        noteTitle = findViewById(R.id.note_title)
        noteContent = findViewById(R.id.note_content)
    }

    private fun saveUpdatedNote() {

    }

    companion object {
        const val noteArrayPosition: Int = 0
    }
}
