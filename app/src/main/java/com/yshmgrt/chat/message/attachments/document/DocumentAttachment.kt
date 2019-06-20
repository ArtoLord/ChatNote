package com.yshmgrt.chat.message.attachments.document

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.beust.klaxon.Klaxon
import com.yshmgrt.chat.MainActivity
import com.yshmgrt.chat.data_base.dataclasses.Attachment
import com.yshmgrt.chat.message.attachments.IAttachment

class DocumentAttachment(val attachment: Attachment) : IAttachment {

    private val document = Klaxon().parse<Document>(attachment.link) ?: Document("","")

    override fun getMessageView(context: Context) = DocumentAttachmentView(context).apply { setContent(document) }

    override fun getPreview(context: Context) = DocumentAttachmentView(context, true).apply { setContent(document) }

    override fun onSended(context: Context) {}

    companion object {
        fun create(document: Document) : Attachment {
            return Attachment(
                123,
                Attachment.DOCUMENT_TYPE.toString(),
                Klaxon().toJsonString(document),
                1
            )
        }

        fun sendIntentToPick(activity: AppCompatActivity) {
            if (ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MainActivity.PERMISSION_REQUEST)
            } else {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "*/*"
                activity.startActivityForResult(intent, MainActivity.PIC_FILE_REQUEST)
            }
        }
    }
}