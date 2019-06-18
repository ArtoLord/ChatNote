package com.yshmgrt.chat.message.attachments.notification

import android.content.Context
import com.beust.klaxon.Klaxon
import com.yshmgrt.chat.data_base.dataclasses.Attachment
import com.yshmgrt.chat.message.attachments.IAttachment

class NotificationAttachment(val attachment: Attachment) : IAttachment {
    override fun onSended(context: Context) {

    }

    private val notification = Klaxon().parse<Notification>(attachment.link)!!

    override fun getMessageView(context : Context) = NotificationAttachmentView(context).apply { setContent(notification) }

    override fun getPreview(context : Context) = getMessageView(context)

    companion object {
        fun create(notification: Notification) : Attachment {
            return Attachment(
                1,
                Attachment.EVENT_TYPE.toString(),
                Klaxon().toJsonString(notification),
                1
            )
        }
    }
}