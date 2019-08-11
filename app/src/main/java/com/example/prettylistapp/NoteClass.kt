package com.example.prettylistapp

import android.util.Log
import com.example.prettylistapp.Files.getFilesNotes
import com.example.prettylistapp.Files.updateNoteInAdress
import java.io.File

class Note(setId: String?, setTitle: String, setContent: String) {

    val id:String? = setId
    private var title = setTitle
    private var content = setContent

    init {

        Log.d("Note created", "Note class instance has id $id, title of $title, content of $content")
    }

    fun getTitle(): String { return title }
    fun getContent(): String { return content }

    fun updateTitle(newTitle: String) { title = newTitle }

    fun updateContent(newCont: String) { content = newCont }

    companion object FilesAddressManager {

        private val listFilesAddress: MutableList<Note> = mutableListOf()
        private val listAddresses: MutableList<String> = mutableListOf()
        private val mTemp: MutableList<Note> = mutableListOf()

        fun clearList() {

            mTemp.addAll(listFilesAddress)
            listFilesAddress.clear()
            listFilesAddress.addAll(mTemp)
            mTemp.clear()
        }


        fun getListFiles(): MutableList<Note> {
            return listFilesAddress
        }

        fun initializeList(filesDir: File) {
            listFilesAddress.clear()
            listFilesAddress.addAll(getFilesNotes(filesDir))
        }

        fun updateItemInserted(note: Note) {
            listFilesAddress.add(0, note)
        }

        fun updateItemRemoved(position: Int) {
            listFilesAddress.removeAt(position)
        }

        //can also call with list of Note objects and then remove that way...
        fun removeFromList(listRemovedIndex: List<Int>) {

            for (index in listRemovedIndex) {
                this.listFilesAddress.removeAt(index)
            }
        }
    }

}