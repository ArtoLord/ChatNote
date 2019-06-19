package com.yshmgrt.chat.data_base.dataclasses

data class Tag(val _id:Long, val text:String, val type:Int){
    companion object{
        val USER_TYPE = 0
        val PARENT_TYPE = 1
    }
}

