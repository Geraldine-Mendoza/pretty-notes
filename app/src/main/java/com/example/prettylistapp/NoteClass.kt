package com.example.prettylistapp

import android.util.Log
import com.example.prettylistapp.Files.getFilesNotes
import com.example.prettylistapp.Files.getLastNoteAdded
import java.io.File

class Note(setId: String?, t: String, cont: String) {

    val id:String? = setId
    val title = t
    val content = cont

    init {

        Log.d("Note created", "Note class instance has id $id, title of $title, content of $content")
    }

    companion object FilesAddressManager {

        private val listFilesAddress: MutableList<Note> = mutableListOf()

        fun getListFiles(): MutableList<Note> {
            return listFilesAddress
        }

        fun initializeList(filesDir: File) {
            listFilesAddress.clear()
            listFilesAddress.addAll(getFilesNotes(filesDir))
        }

        fun updateItemInserted(filesDir: File) {
            addToBeginningList(getLastNoteAdded(filesDir))
        }

        private fun addToBeginningList(addNote: Note) {
            this.listFilesAddress.add(0, addNote)
        }

        //can also call with list of Note objects and then remove that way...
        fun removeFromList(listRemovedIndex: List<Int>) {

            for (index in listRemovedIndex) {
                this.listFilesAddress.removeAt(index)
            }
        }

        //i dont think this would ever be used
        fun addList(list: List<Note>) {
            for (note in list) {
                addToBeginningList(note)
            }
        }
    }

}