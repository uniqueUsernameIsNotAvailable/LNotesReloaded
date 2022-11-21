package com.hexahexagon.lnotes.db

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hexahexagon.lnotes.R
import com.hexahexagon.lnotes.databinding.ListNameItemBinding
import com.hexahexagon.lnotes.entities.TodoLists

class TodoNameAdapter(private val listener: Listener) :
    ListAdapter<TodoLists, TodoNameAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position), listener)
    }


    class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ListNameItemBinding.bind(view)

        fun setData(todoNameItem: TodoLists, listener: Listener) = with(binding) {
            tvTitle.text = todoNameItem.name
            tvTime.text = todoNameItem.time


            //itemView.setOnClickListener { listener.onClickItem(note) }


            btnDel.setOnClickListener { listener.deleteItem(todoNameItem.id!!) }

            btnEdit.setOnClickListener { listener.editItem(todoNameItem) }
        }

        companion object {
            fun create(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.list_name_item, parent, false)
                )
            }
        }

    }

    class ItemComparator : DiffUtil.ItemCallback<TodoLists>() {
        override fun areItemsTheSame(oldItem: TodoLists, newItem: TodoLists): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TodoLists, newItem: TodoLists): Boolean {
            return oldItem == newItem
        }

    }

    interface Listener {
        fun deleteItem(id: Int)
        fun editItem(todoNameItem: TodoLists)
        fun onClickItem(todoNameItem: TodoLists)
    }
}