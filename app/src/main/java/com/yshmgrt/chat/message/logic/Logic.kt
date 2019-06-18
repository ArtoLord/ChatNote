package com.yshmgrt.chat.message.logic

class Logic {
    private var tags = emptyList<String>()
    private var messageText: String

    constructor (message: String) {
        this.messageText = message
    }
    fun getTags(): List<String> {
        val lat = Regex(pattern = "#[А-Яа-яA-Za-z0-9_]+")
        tags = lat.findAll(messageText).toList().map {
            val a = it.value
            it.value.slice(1 until a.length)
        }.map { it.toLowerCase() }

        return tags
    }
}