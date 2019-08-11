package com.example.prettylistapp

//import android.content.Context
import android.app.Activity
import android.content.Intent
//import android.graphics.Color
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
//import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.selection.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prettylistapp.Adapter.Companion.tracker
//import com.example.prettylistapp.Files.getFilesNotes
//import com.example.prettylistapp.Files.getLastNoteAdded


import kotlinx.android.synthetic.main.activity_main.*


//now need to update recyclerView after return from newNote/noteInspection


/**      **   TO DO !!!  **

    ADD ERROR ALERTS ALWAYS WHEN CHECKING IF SOMETHING IS OK
    organize saveFile/getFile functions and how we get filesDir
    re-add newNote supportActionBar
    add space between title and text now that extra bottom space was taken out
 *  need to have a "temp" list of Notes so that we can give it to real list after it is cleared EVERYTIME bc recyclerView sucks

*/

// USE OBJECT (SINGLETON) TO STORE ~CACHE OF DATA... PLACE ADD FUNCTION WITHIN THERE, OR ADD A COMPANION OBJECT done!
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
 * - space between title and content is too large because of editText style of title DONE (was bc of println instead of print())
 * - larger margin at sides of note DONE
 * - change title font
 * - textured note background
 *
 *
 */

//types of request codes
val CREATE_NEW_NOTE = 1
val MODIFY_NOTE = 2

//result codes
val ERROR_SAVING = 3
val DELETE_NOTE = 4
val CHANGE_NOTE = 5

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    //private lateinit var recyclerViewX: androidx.recyclerview.widget.RecyclerView
    private lateinit var adapter: RecyclerView.Adapter<*>
    private val listFilesAddress: MutableList<Note> = Note.getListFiles()

    //keep track of selection logic
    private var itemsAreSelected: Boolean = false

    //selection menu item itemId
    private val tagButtonId = Menu.FIRST
    private val deleteButtonId = tagButtonId+1
    private val cancelButtonId = tagButtonId+2

    companion object {
        const val changedNotePosition: Int = 0
    }

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
        adapter = Adapter(recyclerView.context, Note.getListFiles())
        recyclerView.adapter = adapter

        //layout manager
        recyclerView.layoutManager = LinearLayoutManager(this)

        //get all stored notes and put in list
        Note.initializeList(filesDir)

        //set up tracker
        val tracker = setUpTracker()
        Adapter.tracker = tracker

        if(savedInstanceState != null)
            tracker?.onRestoreInstanceState(savedInstanceState)

    }

    //so that tracker does not 'restart' on flip and various other stuff
    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        if(outState != null)
            tracker?.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    //update items on selection of at least one item
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {

        if (itemsAreSelected) {
            menu?.removeItem(R.id.action_settings)
            menu?.add(0, deleteButtonId, Menu.NONE, R.string.delete_button)?.setIcon(R.drawable.ic_trash_white_24dp)
                ?.setShowAsAction(SHOW_AS_ACTION_ALWAYS)
            menu?.add(0, tagButtonId, Menu.NONE, R.string.tag_button)?.setIcon(R.drawable.ic_label_full_white_24dp)
                ?.setShowAsAction(SHOW_AS_ACTION_IF_ROOM)
            menu?.add(0, cancelButtonId, Menu.NONE, R.string.cancel_select_button)?.setIcon(R.drawable.ic_close_white_24dp)
                ?.setShowAsAction(SHOW_AS_ACTION_IF_ROOM)

        }

        //automatically goes back to original menu

        return true
    }

    //options item (so far) only needed for onSelected menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true //open settings
            cancelButtonId -> { deselectAllItems(); Log.d("menu selection", "here we are, clicking the cancel button"); return true}
            deleteButtonId -> { true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //handle actions onReturn (update recyclerView depending on activity action)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == Activity.RESULT_CANCELED) {
            Log.d("result", "cancelled result")

            //if list is not cleared and notifydatachanged, app will crash on return from newNote (after no note made)
            //and back button pressed

            Note.clearList()
            adapter.notifyDataSetChanged()
        } else if (resultCode == ERROR_SAVING) {
            Note.clearList()
            adapter.notifyDataSetChanged()
            errorToastAlert("error saving note.", recyclerView.context)
        } else {

            //do any errors in copying appear due to getting note?
            when (requestCode) {
                CREATE_NEW_NOTE -> {

                    if (resultCode == Activity.RESULT_OK) { updateInsertedItem() }
                }

                MODIFY_NOTE -> {

                    val changedNoteAt = intent.getIntExtra("changedNotePosition", 0)

                    when (resultCode) {

                        DELETE_NOTE -> { updateDeletedItem(changedNoteAt)}

                        CHANGE_NOTE -> { updateModifiedItem(changedNoteAt) }
                    }

                }
            }
        }
    }

    //fabButton to make new Note
    private fun onAddClick(v: View) {

        //creating small intent test
        val testIntent = Intent(v.context, NewNote::class.java)
        startActivityForResult(testIntent, CREATE_NEW_NOTE)
    }

    //these update fuction both update the recycler view AND the listFIlesAddress which both this and Adapter depend on!
    /** EACH FUNCTION MUST BE CALLED APPROPIATELY AND UPDATEALLITEMS SHOULD BE USED CAREFULLY*/
    //what could also be done is that listFilesAddress is updated from within the Files functions, and then we simply get the list again from Note class...

    //does not work
    //called only after returning (with trash result) from inspection activity
    private fun updateDeletedItem(deletedItemPosition: Int) {

        adapter.notifyItemRemoved(deletedItemPosition)
        adapter.notifyItemRangeChanged(deletedItemPosition, listFilesAddress.size-deletedItemPosition)

    }

    //called only after returning (with correct result) from inspection activity
    private fun updateModifiedItem(itemPosition: Int) {

        adapter.notifyItemChanged(itemPosition)
        adapter.notifyItemRangeChanged(itemPosition, listFilesAddress.size-itemPosition)
    }

    //called only after new note activity
    private fun updateInsertedItem() {

        //list is updated in newNote.kt, here we just need to update adapter (recyclerView)
        Log.d("list file", "this is list of Note objects: $listFilesAddress at updateInserted Item has size ${listFilesAddress.size}")
        //adapter.notifyItemInserted(0) //--> seems like not really needed

        //APP WILL CRASH UNLESS YOU UPDATE ITEM RANGE ALWAYSSSSS ;((
        adapter.notifyItemRangeChanged(0, listFilesAddress.size)

        //scroll to position of item added
        recyclerView.scrollToPosition(0)

    }

    //called when multiple items are deleted at the same time... must also update range... :(
    private fun updateAllItems() {

        Log.d("resume", "we are now resuming")
        Note.initializeList(filesDir)
        Log.d("list file", "this is list of Note objects: ")

        val temp = mutableListOf<String>()
        for (note in listFilesAddress) {
            temp.add(note.getTitle())
        }

        Log.d("file list", "$temp")
        temp.clear()

        adapter.notifyDataSetChanged()

        //have to call this after deletion!
        //adapter.notifyItemRangeChanged(getPosition, mDataSet.size())
    }

    //set up tracker, actions when at least one item is selected
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

                        //instantly changes menu to update (updates using prepare menu)
                        invalidateOptionsMenu()
                    }
                    //pass to whatever function wants them?

                    //on first selection, notify that we need button to delete in action bar

                }
            }
        )

        return tracker
    }

    private fun deselectAllItems() {

        Log.d("menu selection", "trying to clear selection after click")
        tracker?.clearSelection()
    }

}
