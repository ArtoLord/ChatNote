package com.yshmgrt.chat.view

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.yshmgrt.chat.R

class MessageDialog(private val actions : List<() -> Unit>) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context as AppCompatActivity)
        builder.setItems(R.array.message_dialog) { _, i ->
            try {
                actions[i].invoke()
            } catch (e : Exception) {}
        }
        return builder.create()
    }
}