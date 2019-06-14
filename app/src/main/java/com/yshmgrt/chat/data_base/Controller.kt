package com.yshmgrt.chat.data_base

import android.content.Context
import com.yshmgrt.chat.data_base.Helper.Helper
import com.yshmgrt.chat.data_base.dataclasses.*
import com.yshmgrt.chat.database
import org.jetbrains.anko.db.*
import org.jetbrains.anko.doAsync
import java.util.*

class Controller(private val ctx: Context):IController {
    override fun getMessageById(_id: Long, callback: Callback<Message>) {
            try {
                callback.onBegin()
                ctx.database.use {
                    select("Message")
                        .whereArgs(
                            "_id = {id}",
                            "id" to _id
                        )
                        .exec {
                            val sqlMessage = parseSingle(classParser<SQL_Message>())
                            ctx.database.use {
                                select("Link", "tagId")
                                    .whereArgs("messageId = {message_id}", "message_id" to sqlMessage._id)
                                    .exec {
                                        val tagList = parseList(object : MapRowParser<Long> {
                                            override fun parseRow(columns: Map<String, Any?>): Long {
                                                return columns.getValue("tagId").toString().toLong()
                                            }
                                        })
                                        ctx.database.use {
                                            select("Attachment", "_id")
                                                .whereArgs("parentId = {message_id}", "message_id" to sqlMessage._id)
                                                .exec {
                                                    val attachmentList = parseList(object : MapRowParser<Long> {
                                                        override fun parseRow(columns: Map<String, Any?>): Long {
                                                            return columns.getValue("_id").toString().toLong()
                                                        }
                                                    })
                                                    callback.onEnd(Message(sqlMessage._id,sqlMessage.text,attachmentList,tagList,
                                                        Date(sqlMessage.time)))

                                                }
                                        }
                                    }
                            }
                        }
                }
            }
            catch (e:Exception){
                callback.onFailure()
            }
    }

    fun addMessage(message: SQL_Message, callback: Callback<Long>){
            ctx.database.use {
                callback.onEnd(insert(
                    "Message",
                    "text" to message.text,
                    "time" to message.time
                ))
            }

    }

    fun getAllMessageId(callback: Callback<List<Long>>){
            try {
                callback.onBegin()
                ctx.database.use {
                    select("Message","_id")
                        .exec {
                            callback.onEnd(
                                parseList(object : MapRowParser<Long> {
                                    override fun parseRow(columns: Map<String, Any?>): Long {
                                        return columns.getValue("_id").toString().toLong()
                                    }
                                })
                            )
                        }
                }
            }
            catch (e:Exception){
                callback.onFailure()
            }

    }

    override fun getTagById(_id: Long, callback: Callback<Tag>) {
            try {
                callback.onBegin()
                ctx.database.use {
                    select("Tag")
                        .whereArgs("_id = {id}", "id" to _id)
                        .exec {
                            callback.onEnd(
                                parseSingle(classParser())
                            )
                        }
                }
            }
            catch (e:Exception){
                callback.onFailure()
            }

    }

    override fun getAttachmentById(_id: Long, callback: Callback<Attachment>) {
            try {
                callback.onBegin()
                ctx.database.use {
                    select("Attachment")
                        .whereArgs("_id = {id}", "id" to _id)
                        .exec {
                            callback.onEnd(
                                parseSingle(classParser())
                            )
                        }
                }
            }
            catch (e:Exception){
                callback.onFailure()
            }

    }

    fun addTag(tag:Tag, callback: Callback<Long>){
            ctx.database.use {
                callback.onEnd(
                insert(
                    "Tag",
                    "text" to tag.text
                )
                )
            }

    }
    fun addAttachment(tag:Attachment, callback: Callback<Long>){
            ctx.database.use {
                callback.onEnd(
                insert(
                    "Attachment",
                    "type" to tag.type,
                    "link" to tag.link,
                    "parentId" to tag.parentId
                ))
            }

    }

    fun addLink(tag: Link, callback: Callback<Long>){
            ctx.database.use {
                callback.onEnd(
                insert(
                    "Link",
                    "messageId" to tag.messageId,
                    "tagId" to tag.tagId
                ))
            }

    }
    fun updateMessage(message:SQL_Message){
        ctx.database.use {
            update(
                "Message",
                "text" to message.text,
                "time" to message.time
            )
                .whereArgs("_id = {id}", "id" to message._id)
                .exec()
        }
    }
}