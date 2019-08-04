package com.example.prettylistapp

//import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

//reference: https://proandroiddev.com/a-guide-to-recyclerview-selection-3ed9f2381504
class MyItemDetailsLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<Long>() {

    override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(event.x, event.y)

        view?.let {
            return (recyclerView.getChildViewHolder(it) as Adapter.ViewHolder).getItemDetails()
        }

        return null
    }

}