package com.example.prettylistapp.Files

import android.util.Log
import com.example.prettylistapp.Note
import java.io.File
import java.io.PrintWriter


fun updateNoteInAdress(position: Int, filesDir: File) {

    val note = Note.getListFiles()[position]
    val arrayNoteProperties = listOf<String>(note.getTitle(), note.getContent())

    note.id?.let {
        val noteFolder = File(filesDir, "notes/$it")

        if (!noteFolder.exists()) {
            //error("note folder does not exist.") //search up on this
            Log.d("file saving", "folder ${noteFolder.path} doesnt exist!")
        }

        writeToAllFiles(noteFolder, arrayNoteProperties)
    }
}

//need to redo all :( no
fun tryToSaveFile(fileName: String, appMainDir: File, noteProperties: List<String>) {

    //make 'notes' dir
    var success = true

    //create new 'notes'folder if it doesnt already exist
    //DO THIS AT START OF APP~
    val dirNotesMain = File(appMainDir, "notes/")
    if (!appMainDir.exists()) {
        success = dirNotesMain.mkdirs() //notes folder
    }

    if (!success) {
        Log.d("file saving", "could not create directory /notes")
        return
    }

    //creating inner folder within files, name of which is id....
    Log.d("saving file", "file name should be $fileName")
    Log.d("saving file", "path name is $dirNotesMain / $fileName")
    val noteDir = File(dirNotesMain, "$fileName/") //it is fileName
    success = noteDir.mkdirs()

    //able to make /notes/uniqueFileName: create file for properties
    if (success) {

        writeToAllFiles(noteDir, noteProperties)

        Log.d("saving file", "we should have directory ${noteDir.path}")

    } else {
        Log.d("file saving", "could not create directory /notes/uniqueFileName ")
        return
    }
}

fun writeToAllFiles(noteDir: File, noteProperties: List<String>) {

    for ((index, file_name) in notePropertyFileNames.withIndex()) {

        val dest = File(noteDir, file_name)
        Log.d("saving file", "file path is ${dest.path}")
        val value = noteProperties[index]

        //try to write
        try {
            // response is the data written to file
            PrintWriter(dest).use { out -> out.print(value) }

        } catch (e: Exception) {
            // handle the exception
            Log.d("file saving", "unable to save $value to $file_name")

        }
    }

}

//i dont think this is a reliable way to create id
//is this too public?
fun createFileName(): String? {

    //try to create unique file name 10 times
    for (i in 1..10) {
        val len = 10
        val charChoices = "ABCDEFGHIJKLMNOTPQWYZabcdefghijklmnopqtrsvwyz1234567890"

        val randomString = (1..len)
            .map { _ -> kotlin.random.Random.nextInt(0, charChoices.length) }
            .map(charChoices::get)
            .joinToString("")

        val posFile = File(randomString) //will this check folder names?

        if (!posFile.exists()) {
            return randomString
        }
    }

    Log.d("new file", "cannot create unique file name")
    return null
}