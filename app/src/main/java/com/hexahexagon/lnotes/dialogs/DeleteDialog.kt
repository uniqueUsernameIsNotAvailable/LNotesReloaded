package com.hexahexagon.lnotes.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.hexahexagon.lnotes.databinding.DeleteDialogBinding
import com.hexahexagon.lnotes.databinding.NewListDialogBinding

object DeleteDialog {
    fun showDialog(context: Context, listener: Listener) {
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        val binding = DeleteDialogBinding.inflate(LayoutInflater.from(context))

        builder.setView(binding.root)
        binding.apply {
            bDel.setOnClickListener {
                listener.onClick()
                dialog?.dismiss()
            }

            bCancel.setOnClickListener {
                dialog?.dismiss()
            }
        }

        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(null)
        dialog.show()
    }

    interface Listener {
        fun onClick()
    }
}