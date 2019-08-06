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


fun updateNoteInAdress(position: Int, filesDir: File) {

    val note = Note.getListFiles()[position]
    val arrayNoteProperties = listOf<String>(note.getTitle(), note.getContent())

    note.id?.let {
        val noteFolder = File(filesDir, "notes/$it")

        if (!noteFolder.exists()) {
            //error("note folder does not exist.") //search up on this
            Log.d("file saving", "folder ${noteFolder.path} doesnt exist!")
        }

        for ( (index, file) in notePropertyFileNames.withIndex() ) {

            val fileToWriteTo = File(noteFolder, file)
            val value = arrayNoteProperties[index]

            //try to write
            try {
                // response is the data written to file
                Log.d("file saving", "saving value of $value")
                PrintWriter(fileToWriteTo).use { out -> out.print(value) }

            } catch (e: Exception) {
                // handle the exception
                Log.d("file saving", "unable to save $value to $fileToWriteTo")

            }

        }
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

    val newNote = Note(null, setTitle = propertyValues[0], setContent = propertyValues[1])

    return newNote

}