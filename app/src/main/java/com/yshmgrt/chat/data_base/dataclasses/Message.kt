package com.yshmgrt.chat.data_base.dataclasses

import java.util.*

data class Message(val _id:Long, val text: String, val attachment: List<Long> /*id*/, val tags: List<Long>/*id*/, val time:Date){
    override fun toString(): String {
        var retString = "$_id: $text \n"
        for (i in tags){
            retString+=" $i \n"
        }
        return retString
    }
}

data class SQL_Message(val _id:Long, val text: String, val time:Long)