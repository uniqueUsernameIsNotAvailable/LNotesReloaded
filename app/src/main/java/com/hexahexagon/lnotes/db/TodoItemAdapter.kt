package com.hexahexagon.lnotes.db

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hexahexagon.lnotes.R
import com.hexahexagon.lnotes.databinding.LibraryItemBinding
import com.hexahexagon.lnotes.databinding.TodoItemBinding
import com.hexahexagon.lnotes.entities.TodoItem

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

        fun setItemData(todoItem: TodoItem, listener: Listener) {
            val binding = TodoItemBinding.bind(view)
            binding.apply {
                tvTitle.text = todoItem.name
                tvInfo.text = todoItem.itemInfo
                tvInfo.visibility = getVisibility(todoItem)
                cbCheck.isChecked = todoItem.itemChecked
                setCheckedAppearance(binding)
                cbCheck.setOnClickListener {
                    listener.onClickItem(todoItem.copy(itemChecked = cbCheck.isChecked), CHECK)
                }
                imUpd.setOnClickListener {
                    listener.onClickItem(todoItem, EDIT)
                }
            }
        }

        fun setLibData(todoItem: TodoItem, listener: Listener) {
            val binding = LibraryItemBinding.bind(view)
            binding.apply {
                tvTitle.text = todoItem.name
                imUpd.setOnClickListener {
                    listener.onClickItem(todoItem, EDIT_LIB_ITEM)
                }
                imDel.setOnClickListener { listener.onClickItem(todoItem, DELETE_LIB_ITEM) }
                itemView.setOnClickListener{listener.onClickItem(todoItem, INSERT)}
            }
        }

        private fun setCheckedAppearance(binding: TodoItemBinding) {
            binding.apply {
                if (cbCheck.isChecked) {
                    tvTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    tvTitle.setTextColor(ContextCompat.getColor(binding.root.context, R.color.gray))
                    tvInfo.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    tvInfo.setTextColor(ContextCompat.getColor(binding.root.context, R.color.gray))
                } else {
                    tvTitle.paintFlags = Paint.ANTI_ALIAS_FLAG
                    tvTitle.setTextColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.black
                        )
                    )
                    tvInfo.paintFlags = Paint.ANTI_ALIAS_FLAG
                    tvInfo.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
                }
            }
        }

        private fun getVisibility(todoItem: TodoItem): Int {
            return if (todoItem.itemInfo.isNullOrEmpty()) {
                View.GONE
            } else
                View.VISIBLE
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
                        .inflate(R.layout.library_item, parent, false)
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
        fun onClickItem(todoItem: TodoItem, state: Int)
    }

    companion object {
        const val EDIT = 0
        const val CHECK = 1
        const val EDIT_LIB_ITEM = 2
        const val DELETE_LIB_ITEM = 3
        const val INSERT = 4
    }
}