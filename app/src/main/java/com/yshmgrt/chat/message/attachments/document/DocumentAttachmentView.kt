package com.yshmgrt.chat.message.attachments.document

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.yshmgrt.chat.R
import java.io.File
import java.net.URI
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.yshmgrt.chat.MainActivity


class DocumentAttachmentView(context: Context, isPreview: Boolean = false) : LinearLayout(context) {
    init {
        if (isPreview)
            LayoutInflater.from(context).inflate(R.layout.document_attachment_preview, this, true)
        else
            LayoutInflater.from(context).inflate(R.layout.document_attachment_view, this, true)
    }

    fun setContent(document: Document) {
        val file = File(document.path)
        findViewById<TextView>(R.id.file_name).text = file.name
        findViewById<TextView>(R.id.file_size).text = file.length().toString()

        setOnClickListener {
            val myIntent = Intent(Intent.ACTION_VIEW)
            myIntent.data = Uri.fromFile(file)
            val j = Intent.createChooser(myIntent, "Choose an application to open with:")
            (context as MainActivity).startActivity(j)
        }
    }

}