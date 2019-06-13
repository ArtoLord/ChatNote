package com.yshmgrt.chat.data_base.dataclasses

import java.util.*

data class Message(val _id:Long, val text: String, val attachment: MutableList<Attachment>, val tags: MutableList<Tag>, val time:Date)