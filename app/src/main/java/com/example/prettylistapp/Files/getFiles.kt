package com.example.prettylistapp.Files

import com.example.prettylistapp.Note
import java.io.File
import java.io.FileInputStream
import java.security.AccessController.getContext

val notePropertyFileNames: List<String> = listOf("title.txt", "content.txt")
//public var listFilesAddress = mutableListOf<String>()

//val filesDir = getContext().getDir


fun turnAddressToNote(file: File): Note {

    val id = file.path.toString().takeLast(10) //takes end of file name
    val propertyValues: MutableList<String> = mutableListOf()

    //get properties
    for (property in notePropertyFileNames) {

        val dest = File(file, property)
        val inputAsString = FileInputStream(dest).bufferedReader().use { it.readText() }
        propertyValues.add(inputAsString)

    }

    return Note(setId = id, t = propertyValues[0], cont = propertyValues[1])
}


//filesDir --> top folder app
fun getFilesNotes(filesDir: File): MutableList<Note> {

    //listFile() -> in app main folder
    // File.listFiles() --> in specific path
    val f = File(filesDir, "notes")
    val fileList = f.listFiles()

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

//NO LONGER NEEDED SINCE DATABASE IS MADE UP OF NOTE OBJECTS
//return instance of Note class
//not including id...
fun getNoteProperties(fileIdAddress: String): Note {

    val noteFolder = File(fileIdAddress)
    val propertyValues: MutableList<String> = mutableListOf()

    for (notePropertyFile in notePropertyFileNames) {

        val dest = File(noteFolder, notePropertyFile)
        val inputAsString = FileInputStream(dest).bufferedReader().use { it.readText() }

        /** adds to END of list */
        propertyValues.add(inputAsString)
    }

    val newNote = Note(null, t = propertyValues[0], cont = propertyValues[1])

    return newNote

}