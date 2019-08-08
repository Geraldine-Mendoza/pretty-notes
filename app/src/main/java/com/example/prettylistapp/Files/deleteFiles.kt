package com.example.prettylistapp.Files

import android.util.Log
import com.example.prettylistapp.Note
import java.io.File
import java.text.FieldPosition


//print position to check...
//pass either note(s) or id...
fun deleteSelectedFiles(selectedPositions: List<Int>, masterDir: File) {

    Log.d("position", "deleting notes with positions $selectedPositions")

    val filesDir = File(masterDir, "notes")
    for (selectedNoteIndex in selectedPositions) {

        deleteFileHelper(selectedNoteIndex, filesDir)

    }

}

fun deleteSingleFile(position: Int, masterDir: File) {

    Log.d("deleting file", "notes before: ${Note.getListFiles()}")
    Log.d("position", "deleting note with position $position")

    deleteFileHelper(position, File(masterDir, "notes"))

    Log.d("deleting file", "notes after: ${Note.getListFiles()}")
}

fun deleteFileHelper(selectedNoteIndex: Int, filesDir: File) {

    val selectedNote = Note.getListFiles()[selectedNoteIndex]
    val folderToDelete = File(filesDir, selectedNote.id!!)

    try {
        folderToDelete.deleteRecursively()
    } catch (e: Exception) {
        Log.d("delete file", "unable to delete file with position $selectedNoteIndex")
    }
}