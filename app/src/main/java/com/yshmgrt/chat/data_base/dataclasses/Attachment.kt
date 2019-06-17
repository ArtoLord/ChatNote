package com.yshmgrt.chat.data_base.dataclasses

class Attachment(val _id:Long,val type:String,val link:String,val parentId:Long){
    companion object {
        val IMAGE_TYPE = 0
        val EVENT_TYPE = 1
        val MESSAGE_TYPE = 2
    }
}
