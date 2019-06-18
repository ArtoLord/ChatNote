package com.yshmgrt.chat.message.attachments.notification

import android.content.Context
import com.beust.klaxon.Klaxon
import com.yshmgrt.chat.data_base.Controller
import com.yshmgrt.chat.data_base.dataclasses.Attachment
import com.yshmgrt.chat.data_base.dataclasses.SQL_Message
import com.yshmgrt.chat.message.attachments.IAttachment

class NotificationAttachment(val attachment: Attachment) : IAttachment {
    override fun onSended(context: Context) {
        val controller = Controller(context)
        controller.getMessageById(attachment.parentId){
            val notification = Klaxon().parse<Notification>(attachment.link)
            controller.resendMessage(attachment.parentId,notification!!.time)
        }
    }

    private val notification = Klaxon().parse<Notification>(attachment.link)!!

    override fun getMessageView(context : Context) = NotificationAttachmentView(context).apply { setContent(notification) }

    override fun getPreview(context : Context) = NotificationAttachmentView(context, true).apply { setContent(notification) }

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