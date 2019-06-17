package com.yshmgrt.chat.message.attachments

import android.content.Context
import android.content.Intent
import android.view.View
import com.yshmgrt.chat.data_base.dataclasses.Attachment

interface IAttachment {
    fun getMessageView(context: Context):View
    fun getPreview(context: Context):View
    companion object {
        fun create(attachment: Attachment): IAttachment? {
            return when (attachment.type.toInt()) {
                Attachment.IMAGE_TYPE -> ImageAttachment(attachment)
                else -> null
            }
        }
    }

}