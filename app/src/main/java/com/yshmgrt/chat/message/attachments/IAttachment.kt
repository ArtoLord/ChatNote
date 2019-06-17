package com.yshmgrt.chat.message.attachments

import android.content.Context
import android.content.Intent
import android.view.View
import com.yshmgrt.chat.data_base.dataclasses.Attachment

interface IAttachment {
    fun getMessageView():View
    fun getPreview():View
    companion object {
        fun create(context: Context,attachment: Attachment): IAttachment? {
            return when (attachment.type.toInt()) {
                Attachment.IMAGE_TYPE -> ImageAttachment(context,attachment)
                else -> null
            }
        }
    }

}