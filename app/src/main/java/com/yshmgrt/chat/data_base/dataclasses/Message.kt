package com.yshmgrt.chat.data_base.dataclasses

import java.util.*

data class Message(val _id:Long, val text: String, val attachment: List<Long>, val tags: List<Long>, val time:Date)

data class SQL_Message(val _id:Long, val text: String, val time:Long)