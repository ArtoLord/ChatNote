package com.yshmgrt.chat.message.attachments

import android.content.Context
import android.content.Intent
import android.view.View
import com.yshmgrt.chat.data_base.dataclasses.Attachment
import com.yshmgrt.chat.message.attachments.notification.NotificationAttachment

interface IAttachment {
    fun getMessageView():View
    fun getPreview():View
    companion object {
        fun create(context: Context,attachment: Attachment): IAttachment? {
            return when (attachment.type.toInt()) {
                Attachment.IMAGE_TYPE -> ImageAttachment(context, attachment)
                Attachment.EVENT_TYPE -> NotificationAttachment(context, attachment)
                else -> null
            }
        }
    }

}