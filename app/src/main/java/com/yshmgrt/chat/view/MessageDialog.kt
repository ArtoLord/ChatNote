package com.yshmgrt.chat.view

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.yshmgrt.chat.R

class MessageDialog(private val actions : List<() -> Unit>, private val isAdd: Boolean) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context as AppCompatActivity)
        builder.setItems(if (isAdd) R.array.message_dialog_add else R.array.message_dialog_remove) { _, i ->
            try {
                actions[i].invoke()
            } catch (e : Exception) {}
        }
        return builder.create()
    }
}