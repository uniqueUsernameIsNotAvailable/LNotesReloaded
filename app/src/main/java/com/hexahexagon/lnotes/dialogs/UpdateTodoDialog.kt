package com.hexahexagon.lnotes.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.hexahexagon.lnotes.R
import com.hexahexagon.lnotes.databinding.EditTodoDialogBinding
import com.hexahexagon.lnotes.databinding.NewListDialogBinding
import com.hexahexagon.lnotes.entities.TodoItem

object UpdateTodoDialog {
    fun showDialog(context: Context, item: TodoItem, listener: Listener) {
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        val binding = EditTodoDialogBinding.inflate(LayoutInflater.from(context))

        builder.setView(binding.root)
        binding.apply {
            edName.setText(item.name)
            edInfo.setText(item.itemInfo)
            if (item.itemType == 1) edInfo.visibility = View.GONE
            btnSave.setOnClickListener {
                if (edName.text.toString().isNotEmpty()) {
                    listener.onClick(item.copy(
                            name = edName.text.toString(),
                            itemInfo = edInfo.text.toString()
                        )
                    )
                }
                dialog?.dismiss()
            }
        }
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(null)
        dialog.show()
    }

    interface Listener {
        fun onClick(item: TodoItem)
    }
}