package com.facundojaton.mobilenativefirebasetask.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.facundojaton.mobilenativefirebasetask.data.model.DatabaseItem
import com.facundojaton.mobilenativefirebasetask.databinding.LayoutListItemBinding

/**
 * Adapter for items on the ListFragment
 */
class ItemsListAdapter : RecyclerView.Adapter<ItemsListAdapter.ListItemViewHolder>() {

    var onItemSwipe: (itemId: String) -> Unit = { }
    private var items: ArrayList<DatabaseItem> = ArrayList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListItemViewHolder {
        return ListItemViewHolder(LayoutListItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val item = items[holder.bindingAdapterPosition]
        holder.bind(item)
    }

    fun setListItems(list: MutableList<DatabaseItem>?) {
        list?.let {
            items.clear()
            items.addAll(list)
            notifyDataSetChanged()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<DatabaseItem>() {
        override fun areItemsTheSame(oldItem: DatabaseItem, newItem: DatabaseItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: DatabaseItem, newItem: DatabaseItem): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class ListItemViewHolder(val binding: LayoutListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DatabaseItem) {
            binding.item = item
            binding.executePendingBindings()
        }
    }

    fun deleteItem(position: Int){
        onItemSwipe(items[position].id)
    }

    override fun getItemCount(): Int = items.size
}