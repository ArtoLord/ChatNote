package com.yshmgrt.chat.data_base.dataclasses

import java.util.*

data class Message(val _id:Long, val text: String, val attachment: List<Long> /*id*/, val tags: List<Long>/*id*/, val time:Date)

data class SQL_Message(val _id:Long, val text: String, val time:Long)