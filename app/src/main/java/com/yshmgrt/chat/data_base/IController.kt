package com.yshmgrt.chat.data_base

import com.yshmgrt.chat.data_base.dataclasses.Attachment
import com.yshmgrt.chat.data_base.dataclasses.Message
import com.yshmgrt.chat.data_base.dataclasses.Tag

interface IController {
    fun getMessageById(_id:Long):Message
    fun getTagById(_id:Long):Tag
    fun getAttachmentById(_id:Long):Attachment
    fun getMessageByIndex():Message
}