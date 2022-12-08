package com.hexahexagon.lnotes.db

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hexahexagon.lnotes.R
import com.hexahexagon.lnotes.databinding.TodoListBinding
import com.hexahexagon.lnotes.entities.TodoList

class TodoListAdapter(private val listener: Listener) :
    ListAdapter<TodoList, TodoListAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position), listener)
    }


    class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = TodoListBinding.bind(view)

        fun setData(todoNameItem: TodoList, listener: Listener) = with(binding) {
            tvTitle.text = todoNameItem.name
            tvTime.text = todoNameItem.time

            itemView.setOnClickListener { listener.onClickItem(todoNameItem) }

            btnDel.setOnClickListener { listener.deleteItem(todoNameItem.id!!) }

            btnEdit.setOnClickListener { listener.editItem(todoNameItem) }
        }

        companion object {
            fun create(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.todo_list, parent, false)
                )
            }
        }

    }

    class ItemComparator : DiffUtil.ItemCallback<TodoList>() {
        override fun areItemsTheSame(oldItem: TodoList, newItem: TodoList): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TodoList, newItem: TodoList): Boolean {
            return oldItem == newItem
        }

    }

    interface Listener {
        fun deleteItem(id: Int)
        fun editItem(todoNameItem: TodoList)
        fun onClickItem(todoNameItem: TodoList)
    }
}