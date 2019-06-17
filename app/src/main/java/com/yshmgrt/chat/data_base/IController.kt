package com.yshmgrt.chat.data_base

import com.yshmgrt.chat.data_base.dataclasses.Attachment
import com.yshmgrt.chat.data_base.dataclasses.Message
import com.yshmgrt.chat.data_base.dataclasses.Tag

interface IController {
    fun getMessageById(_id:Long, callback: Callback<Message>)
    fun getTagById(_id:Long,callback: Callback<Tag>)
    fun getAttachmentById(_id:Long,callback: Callback<Attachment>)
}

interface  Callback<T>{
    fun onFailure()
    fun onBegin()
    fun onEnd(exit:T)
}