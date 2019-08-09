package com.example.prettylistapp.Files

import android.util.Log
import com.example.prettylistapp.Note
import java.io.File
import java.text.FieldPosition


//print position to check...
//pass either note(s) or id...
fun deleteSelectedFiles(selectedPositions: List<Int>, masterDir: File): Boolean {

    Log.d("position", "deleting notes with positions $selectedPositions")

    val filesDir = File(masterDir, "notes")
    for (selectedNoteIndex in selectedPositions) {

        if (!deleteFileHelper(selectedNoteIndex, filesDir)) {
            return false
        }

    }

    return true
}

fun deleteSingleFile(position: Int, masterDir: File): Boolean {

    Log.d("deleting file", "notes before: ${Note.getListFiles()}")
    Log.d("position", "deleting note with position $position")

    return deleteFileHelper(position, File(masterDir, "notes"))

}

fun deleteFileHelper(selectedNoteIndex: Int, filesDir: File): Boolean {

    val selectedNote = Note.getListFiles()[selectedNoteIndex]
    val folderToDelete = File(filesDir, selectedNote.id!!)
    var success = true

    try {
        folderToDelete.deleteRecursively()
    } catch (e: Exception) {
        success = false
        Log.d("delete file", "unable to delete file with position $selectedNoteIndex")
    }

    return success
}