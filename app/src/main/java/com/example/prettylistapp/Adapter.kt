package com.example.prettylistapp

import android.app.Activity
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
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
//import com.example.prettylistapp.Files.listFilesAddress
import kotlinx.android.synthetic.main.list_item.view.*
import java.security.AccessController.getContext


//position passed to adapter is the same as position of Note in array
/** does this change after item is deleted? **/

class Adapter(private val activityContext: Context, private val listFilesAddress: MutableList<Note>): RecyclerView.Adapter<Adapter.ViewHolder>() {

    //var listFilesAddress = com.example.prettylistapp.Files.listFilesAddress

    companion object {

        var tracker: SelectionTracker<Long>? = null

        //const val bc each viewHolder class instance is for a specific note
        const val noteArrayPosition:Int = 0
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

    //added inner so that i have access to variables in adapter
    inner class ViewHolder(private val inflatedView: View) : RecyclerView.ViewHolder(inflatedView), View.OnClickListener {

        private lateinit var noteTitleText: TextView
        private lateinit var noteContentText: TextView
        val context = inflatedView.context

        //we store position so that we can pass it to inspection onClick
        //private var position: Int

        init {
            inflatedView.setOnClickListener(this)
        }

        fun bindNote(position: Int, activatedBool: Boolean = false) {

            val currNote = listFilesAddress[position]

            Log.d("position", "bind note with position $position")

            //setting up textViews
            noteTitleText = inflatedView.note_title
            noteContentText = inflatedView.note_content

            //getting object with full information from array
            val title = currNote.getTitle()
            val content = currNote.getContent()

            //setting object properties to view
            if (title.isNotEmpty()) { //if 'empty'
                Log.d("bind note", "title of $title")
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
            Log.d("position", "clicked note with position $adapterPosition")

            //intent
            val fullNoteIntent = Intent(context, NoteInspection::class.java) //add class intent will move to

            //add extra info to intent
            fullNoteIntent.putExtra("noteArrayPosition", adapterPosition) //add correct note position in arr
            (activityContext as Activity).startActivityForResult(fullNoteIntent, 2)
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> = object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): Long? = itemId //uses id, which is the position as a Long
        }

    }

}