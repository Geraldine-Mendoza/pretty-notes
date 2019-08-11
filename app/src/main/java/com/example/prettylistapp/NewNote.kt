package com.example.prettylistapp

//import android.support.v7.app.AppCompatActivity
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.prettylistapp.Files.createFileName
import com.example.prettylistapp.Files.tryToSaveFile

class NewNote : AppCompatActivity() {

    private lateinit var titleText: EditText
    private lateinit var contentText: EditText
    private val ERROR_SAVING = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_note)

        //set up action bar
        val actionBar = supportActionBar
        //actionBar!!.title =

        actionBar?.setDisplayHomeAsUpEnabled(true)

        titleText = findViewById(R.id.note_title)
        contentText = findViewById(R.id.note_content)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_new_note, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        //is this a safe null practice?
        when (item?.itemId) {

            R.id.done_button -> doneButtonClicked()
            //is this safe, for back button?
            android.R.id.home -> { setResult(Activity.RESULT_CANCELED); finish() }
            else -> {
                setResult(Activity.RESULT_CANCELED)
                finish()
            }

        }

        return true
    }

    private fun doneButtonClicked() {

        //note is saved regardless of empty title/content

        //creating new Note instance
        val newNote = Note(createFileName(), titleText.text.toString(), contentText.text.toString())
        val context = titleText.context

        //what will be saved
        val noteProperties = listOf(newNote.getTitle(), newNote.getContent())

        //save to internal storage in separate file
        val directory = context.getFilesDir() //internal storage
        Log.d("new file", "here is where we are: ${directory.path}")

        //if note id is not null, save and go back
        newNote.id?.let {

            //save new dir -> if success in saving, return RESULT_OK
            if (tryToSaveFile(it, directory, noteProperties)) {

                //if saved, update in list
                Note.updateItemInserted(newNote)

                val temp = mutableListOf<String>()
                for (note in Note.getListFiles()) {
                    temp.add(note.getTitle())
                }

                Log.d("file list", "after last item is saved, this is list: $temp")
                temp.clear()
                setResult(Activity.RESULT_OK)
            } else {
                setResult(ERROR_SAVING)
            }

            finish()
            return
        }

        //in case id didnt work (was null)
        Log.d("file saving", "error: note/file id is null ($newNote.id)")
        errorToastAlert("could not create unique file name", context)

        //val inputAsString = FileInputStream(file).bufferedReader().use { it.readText() }
    }

}
