package com.example.prettylistapp.Files

import android.util.Log
import com.example.prettylistapp.Note
import com.example.prettylistapp.errorToastAlert
import java.io.File
import java.io.FileInputStream
import java.io.PrintWriter
import java.text.FieldPosition

val notePropertyFileNames: List<String> = listOf("title.txt", "content.txt")

//public var listFilesAddress = mutableListOf<String>()

//val filesDir = getContext().getDir

class notesAdresses {

    companion object {

        //val filesDir =
        val allAdresses: MutableList<String> = mutableListOf()
    }

}

fun turnAddressToNote(file: File): Note {

    val id = file.path.toString().takeLast(10) //takes end of file name
    val propertyValues: MutableList<String> = mutableListOf()

    //get properties
    for (property in notePropertyFileNames) {

        val dest = File(file, property)
        val inputAsString = FileInputStream(dest).bufferedReader().use { it.readText() }
        propertyValues.add(inputAsString)

    }

    return Note(setId = id, setTitle = propertyValues[0], setContent = propertyValues[1])
}

//filesDir --> top folder app
fun getFilesNotes(filesDir: File): MutableList<Note> {

    val fileList = getFileAdresses(filesDir)

    val fileNoteArr: MutableList<Note> = mutableListOf()

    //files saved
    for (file in fileList) {

        //get info from each file
        val note = turnAddressToNote(file)
        fileNoteArr.add(note)

    }

    //Log.d("get files", "files in app: ${Arrays.toString(fileList)}")

    return fileNoteArr.asReversed() //return reverse so that most recent appears first

}

fun getLastNoteAdded(filesDir: File): Note {

    val f = File(filesDir, "notes")
    val fileList = f.listFiles()
    val lastFile = fileList[fileList.size-1] //returning last file --> that is, most recently added

    return turnAddressToNote(lastFile)
}

fun getFileAdresses(filesDir: File): Array<File> {
    //listFile() -> in app main folder
    // File.listFiles() --> in specific path
    val f = File(filesDir, "notes")
    return f.listFiles()
}