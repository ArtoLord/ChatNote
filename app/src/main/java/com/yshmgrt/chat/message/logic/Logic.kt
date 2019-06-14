package com.yshmgrt.chat.message.logic

class Logic {
    private var tags = emptyList<String>()
    private var messageText: String

    constructor (message: String) {
        this.messageText = message
    }
    fun getTags(): List<String> {
        val lat = Regex(pattern = "#[A-Za-z0-9_]+")
        tags = lat.findAll(messageText).toList().map { it.value }

        return tags;
    }
}
//fun main() = Logic(readLine()!!).getTags().forEach { println(it) }