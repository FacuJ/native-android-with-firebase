package com.facundojaton.mobilenativefirebasetask

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.facundojaton.mobilenativefirebasetask.adapters.ItemsListAdapter

class SwipeToDelete(var adapter: ItemsListAdapter): ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean { return false }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val itemPosition = viewHolder.bindingAdapterPosition
        adapter.deleteItem(itemPosition)
    }

}