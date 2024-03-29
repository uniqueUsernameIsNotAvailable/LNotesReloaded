package com.hexahexagon.lnotes.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.hexahexagon.lnotes.R
import com.hexahexagon.lnotes.databinding.NewListDialogBinding

object NewListDialog {
    fun showDialog(context: Context, listener: Listener, name: String) {
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        val binding = NewListDialogBinding.inflate(LayoutInflater.from(context))

        builder.setView(binding.root)
        binding.apply {
            edListName.setText(name)
            if (name.isNotEmpty()) bMakeList.text = context.getString(R.string.update)
            bMakeList.setOnClickListener {
                val listName = edListName.text.toString()
                if (listName.isNotEmpty()) {
                    listener.onClick(listName)
                }
                dialog?.dismiss()
            }
        }
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(null)
        dialog.show()
    }

    interface Listener {
        fun onClick(name: String)
    }
}