package com.example.prettylistapp

//import android.support.v7.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.prettylistapp.Files.deleteSingleFile
import com.example.prettylistapp.Files.updateNoteInAdress
import kotlinx.android.synthetic.main.note_inspection.*

class NoteInspection : AppCompatActivity() {

    private lateinit var noteTitle: EditText
    private lateinit var noteContent: EditText
    private var listFilesAddress = Note.getListFiles()
    private var noteItemSelected: Note? = null
    private var notePosition: Int? = null

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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        //is this a safe null practice?
        when (item?.itemId) {

            //need to timplement result for Main Activity
            R.id.delete_button -> {

                notePosition?.let {
                    //if successful at removing file,
                    if (deleteSingleFile(it, filesDir)) {
                        listFilesAddress.removeAt(it)
                    }

                    val result = Intent()
                    result.putExtra("changedNotePosition", it)
                    setResult(DELETE_NOTE)

                } ?: run {

                    Log.d("inspection", "position is null!")
                    errorToastAlert("note position cannot be found.", noteTitle.context)
                    setResult(ERROR_SAVING)
                }

                finish()
            }

            android.R.id.home -> {

                //update item in list and file before going back
                if (!saveUpdatedNote()) {
                    setResult(ERROR_SAVING)
                } else {

                    val result = Intent()
                    result.putExtra("changedNotePosition", notePosition)
                    setResult(CHANGE_NOTE)
                }

                finish()
            }

            else -> return true

        }

        return true
    }

    //save note whenever back is pressed
    override fun onBackPressed() {
        super.onBackPressed()

        saveUpdatedNote()
    }

    private fun setUpInspection() {

        notePosition = intent.getIntExtra("noteArrayPosition", 0)

        //NOT WELL ORGANIZED
        // if position not within size of array, there was an error!
        if ( notePosition!! !in (0..listFilesAddress.size) || notePosition == null) {
            Log.d("note inspection", "the position is outside of the possible range! position is $notePosition")
            errorToastAlert("requesting note information failed.", noteTitle.context)

            setResult(ERROR_SAVING)
            onBackPressed()
        }

        notePosition?.let {
            noteItemSelected = listFilesAddress[it]

            noteTitle.setText(noteItemSelected?.getTitle())
            noteContent.setText(noteItemSelected?.getContent())
        }
    }

    private fun setUpUIElements() {
        noteTitle = findViewById(R.id.note_title)
        noteContent = findViewById(R.id.note_content)
    }

    private fun saveUpdatedNote(): Boolean {

        Log.d("file saving", "saving note")

        noteItemSelected?.let {

            it.updateTitle(noteTitle.text.toString())
            it.updateContent(noteContent.text.toString())
        }

        notePosition?.let {
            if (!updateNoteInAdress(it, filesDir)) { return false }
        }

        return true

    }

    companion object {
        const val noteArrayPosition: Int = 0
        const val changedNotePosition: Int = 0
    }
}
