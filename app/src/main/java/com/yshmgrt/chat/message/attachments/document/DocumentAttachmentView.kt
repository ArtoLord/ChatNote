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
import androidx.core.content.FileProvider


class DocumentAttachmentView(context: Context, isPreview: Boolean = false) : LinearLayout(context) {
    init {
        if (isPreview)
            LayoutInflater.from(context).inflate(R.layout.document_attachment_preview, this, true)
        else
            LayoutInflater.from(context).inflate(R.layout.document_attachment_view, this, true)
    }

    fun setContent(document: Document) {
        val file = File(document.path)
        findViewById<TextView>(R.id.file_name).text = document.name
        findViewById<TextView>(R.id.file_size).text = sizeToString(file.length())

        setOnClickListener {
            val myIntent = Intent(Intent.ACTION_VIEW)
            myIntent.data = Uri.fromFile(file)
            val apkURI = FileProvider.getUriForFile(
                context,
                context.applicationContext
                    .packageName + ".provider", file
            )
            myIntent.setDataAndType(apkURI, document.ext)
            myIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            (context as MainActivity).startActivity(myIntent)
        }
    }

    private fun sizeToString(size : Long) : String {
        var a = size
        if (a < 1024)
            return "$a bytes"
        a /= 1024
        if (a < 1024)
            return "$a KB"
        a /= 1024
        if (a < 1024)
            return "$a MB"
        a /= 1024
        if (a < 1024)
            return "$a GB"
        a /= 1024
        return "A lot of"
    }

}