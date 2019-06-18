package com.yshmgrt.chat.message.attachments.notification

import android.content.Context
import com.yshmgrt.chat.data_base.dataclasses.Attachment
import com.yshmgrt.chat.message.attachments.IAttachment

class NotificationAttachment(val context : Context, val attachment: Attachment) : IAttachment {
    override fun getMessageView() = NotificationAttachmentView(context)

    override fun getPreview() = getMessageView()
}