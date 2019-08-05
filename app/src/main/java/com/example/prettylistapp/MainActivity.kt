package com.example.prettylistapp

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
//import android.support.v7.app.AppCompatActivity
//import android.support.v7.widget.LinearLayoutManager
//import android.support.v7.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MenuItem.SHOW_AS_ACTION_ALWAYS
import android.view.MenuItem.SHOW_AS_ACTION_IF_ROOM
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prettylistapp.Adapter.Companion.tracker
import com.example.prettylistapp.Files.getFilesNotes
import com.example.prettylistapp.Files.getLastNoteAdded


import kotlinx.android.synthetic.main.activity_main.*


//now need to update recyclerView after return from newNote/noteInspection

//first click expands
// then, button at top to edit, which takes to 'noteInspection'
// second click to close
// USE OBJECT (SINGLETON) TO STORE ~CACHE OF DATA... PLACE ADD FUNCTION WITHIN THERE, OR ADD A COMPANION OBJECT
//
// things that annoy me about other apps
// - scrolling to find/see title should be as each as possible
// - the should be a search function (if possible), either for title or title && content or xor
// - organizing notes into folders/tags should be available
// - color! pretty default! dark mode!

/**
 *   AESTHETIC CONCERNS
 *
 * - actionbar should blend with no shadow... GET OUT MATERIAL I WANT MINIMAL!
 * - space between title and content is too large because of editText style of title
 * - larger margin at sides of note
 * - change title font
 * - textured note background
 *
 *
 */



class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    //private lateinit var recyclerViewX: androidx.recyclerview.widget.RecyclerView
    private lateinit var adapter: RecyclerView.Adapter<*>
    private val listFilesAddress: MutableList<Note> = mutableListOf()

    private var itemsAreSelected: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        fab.setOnClickListener { view ->
            //this is a test
            onAddClick(view)
        }



        //setting up recycler view
        recyclerView = findViewById(R.id.recycler_view)
        //recyclerViewX = findViewById(R.id.recycler_view) //they are both the same, this is gonna cause problems

        //adapter
        adapter = Adapter(recyclerView.context, listFilesAddress)
        recyclerView.adapter = adapter

        //layout manager
        recyclerView.layoutManager = LinearLayoutManager(this)

        //get all stored notes
        listFilesAddress.addAll(getFilesNotes(filesDir))

        //set up tracker
        val tracker = setUpTracker()
        Adapter.tracker = tracker

        if(savedInstanceState != null)
            tracker?.onRestoreInstanceState(savedInstanceState)

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        if(outState != null)
            tracker?.onSaveInstanceState(outState)
    }

    //should not update items here... instead, when returning from add (result)
    override fun onResume() {
        super.onResume()

        updateAllItems()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    /*override fun onPrepareOptionsMenu(menu: Menu?): Boolean {

        if (itemsAreSelected) {
            menu?.removeItem(R.id.action_settings)
            menu?.add(0, Menu.FIRST, Menu.NONE, R.string.select_all_button)?.setIcon(R.drawable.ic_select_all_white_24dp)
                ?.setShowAsAction(SHOW_AS_ACTION_IF_ROOM)

        } else {

        }

        return true
    }*/

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onAddClick(v: View) {

        //creating small intent test
        val testIntent = Intent(v.context, NewNote::class.java)
        startActivity(testIntent)
    }

    private fun updateInsertedItem() {

        listFilesAddress.add(0, getLastNoteAdded(filesDir)) //add most recent (last added) note to beginning of arr
        Log.d("list file", "this is list of Note objects: $listFilesAddress")
        adapter.notifyItemInserted(0)


    }

    private fun updateAllItems() {

        Log.d("resume", "we are now resuming")
        listFilesAddress.clear()
        listFilesAddress.addAll(getFilesNotes(filesDir))
        Log.d("list file", "this is list of Note objects: $listFilesAddress")

        adapter.notifyDataSetChanged()
    }

    private fun setUpTracker(): SelectionTracker<Long> {
        //setting up tracker
        val tracker = SelectionTracker.Builder<Long>(
            "mainItemSelection",
            recyclerView,
            StableIdKeyProvider(recyclerView),
            MyItemDetailsLookup(recyclerView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(SelectionPredicates.createSelectAnything()).build()

        //observer checks how many are selected
        tracker?.addObserver(
            object : SelectionTracker.SelectionObserver<Long>() {
                override fun onSelectionChanged() {
                    super.onSelectionChanged()
                    val items = tracker?.selection?.size()

                    items?.let {
                        if (it > 0) {
                            itemsAreSelected = true
                            title = "$it items selected"
                            supportActionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.barItemsSelected)))
                        } else {
                            itemsAreSelected = false
                            title = "PrettyListApp"
                            supportActionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.colorPrimary)))
                        }
                    }
                    //pass to whatever function wants them?

                    //on first selection, notify that we need button to delete in action bar

                }
            }
        )

        return tracker
    }

    private fun getOldItems() {

        val currItemCount = recyclerView.childCount
        val backUpList = mutableListOf<View>()

        for (index in (0..currItemCount)) {

            //we are getting view holder... this or view?
            val itemViewHolder = recyclerView.getChildAt(index)
        }

    }

    /*
    return object : SelectionTracker.SelectionPredicate<K>() {

        override fun canSelectMultiple(): Boolean {
            return true
        }
    }*/
}
