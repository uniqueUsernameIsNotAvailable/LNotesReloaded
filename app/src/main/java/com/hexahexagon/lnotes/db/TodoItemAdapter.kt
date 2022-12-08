package com.hexahexagon.lnotes.db

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hexahexagon.lnotes.R
import com.hexahexagon.lnotes.databinding.TodoItemBinding
import com.hexahexagon.lnotes.entities.TodoItem
import com.hexahexagon.lnotes.entities.TodoList

class TodoItemAdapter(private val listener: Listener) :
    ListAdapter<TodoItem, TodoItemAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return if (viewType == 0)
            ItemHolder.createTodoItem(parent)
        else
            ItemHolder.createLibItem(parent)

    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        if (getItem(position).itemType == 0) {
            holder.setItemData(getItem(position), listener)
        } else {
            holder.setLibData(getItem(position), listener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).itemType
    }


    class ItemHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val binding = TodoItemBinding.bind(view)

        fun setItemData(todoItem: TodoItem, listener: Listener) {
            val binding = TodoItemBinding.bind(view)
            binding.apply {
                tvTitle.text = todoItem.name
            }

        }

        fun setLibData(todoItem: TodoItem, listener: Listener) {

        }


        companion object {
            fun createTodoItem(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.todo_item, parent, false)
                )
            }

            fun createLibItem(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.todo_list, parent, false)
                )
            }
        }

    }

    class ItemComparator : DiffUtil.ItemCallback<TodoItem>() {
        override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
            return oldItem == newItem
        }

    }

    interface Listener {
        fun deleteItem(id: Int)
        fun editItem(todoNameItem: TodoList)
        fun onClickItem(todoNameItem: TodoList)
    }
}