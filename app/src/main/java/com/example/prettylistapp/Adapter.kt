package com.example.prettylistapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.opengl.Visibility
//import android.support.v4.content.ContextCompat
//import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.example.prettylistapp.Files.getNoteProperties
//import com.example.prettylistapp.Files.listFilesAddress
import kotlinx.android.synthetic.main.list_item.view.*

class Adapter(context: Context, private val listFilesAddress: MutableList<Note>): RecyclerView.Adapter<Adapter.ViewHolder>() {

    //var listFilesAddress = com.example.prettylistapp.Files.listFilesAddress

    companion object {

        var tracker: SelectionTracker<Long>? = null
        var positionsArray = mutableListOf<Long>()

        //const val bc each viewHolder class instance is for a specific note
        const val note_id = "note_id"
    }

    //private val backUpItemList = mutableListOf<View>() //is keeping track of VIEWS ok? will the views here be updated

    init {
        Log.d("adapter", "in adapter...hehe")
        setHasStableIds(true)
    }

    //create new views!
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): Adapter.ViewHolder {
        //set up inflater
        val inflater: LayoutInflater = LayoutInflater.from(parent.context) //not sure if this context is ok, or getContext()

        //inflate view!
        val inflateView = inflater.inflate(R.layout.list_item, parent, false) //attachToRoot should be false!

        //add view to backUpList
        //backUpItemList.add(inflateView)

        return ViewHolder(inflateView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        tracker?.let {
            holder.bindNote(position, it.isSelected(position.toLong()))
        }
    }



    override fun getItemId(position: Int): Long {

        return position.toLong()
    }

    override fun getItemCount(): Int {

        //number of directories (notes)
        return listFilesAddress.size
    }

    fun updateRecyclerView() {
        notifyDataSetChanged()
    }

    //added inner so that i have access to variables in adapter
    inner class ViewHolder(private val inflatedView: View) : RecyclerView.ViewHolder(inflatedView), View.OnClickListener {

        private lateinit var noteTitleText: TextView
        private lateinit var noteContentText: TextView
        val context = inflatedView.context

        init {
            inflatedView.setOnClickListener(this)
        }

        fun bindNote(position: Int, activatedBool: Boolean = false) {

            val currNote = listFilesAddress[position]

            //setting up textViews
            noteTitleText = inflatedView.note_title
            noteContentText = inflatedView.note_content

            //getting object with full information from array
            val title = currNote.title
            val content = currNote.content

            //setting object properties to view
            if (title != "\n") { //if 'empty'
                Log.d("bind note", "title of $title is not empty")
                noteTitleText.visibility = View.VISIBLE
            } else {
                Log.d("bind note", "gonna make you gone")
                noteTitleText.visibility = View.GONE
            }

            noteTitleText.text = title
            noteContentText.text = content
            inflatedView.isActivated = activatedBool

        }

        override fun onClick(v: View?) {
            //on click, create intent to move to note

            //get context using the (previously) inflatedView
            val context = context

            //intent
            val fullNoteIntent = Intent(context, NoteInspection::class.java) //add class intent will move to

            //add extra info to intent
            fullNoteIntent.putExtra("note_id", 0) //add correct note id
            context.startActivity(fullNoteIntent)
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> = object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): Long? = itemId
        }

    }

}