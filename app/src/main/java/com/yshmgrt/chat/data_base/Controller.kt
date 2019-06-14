package com.yshmgrt.chat.data_base

import android.content.Context
import android.util.Log
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

    fun getMessageById(_id:Long,onEnd:(Message) -> Unit){
        getMessageById(_id,object:Callback<Message>{
            override fun onFailure() {}
            override fun onBegin() {}
            override fun onEnd(exit: Message) {onEnd(exit)}
        })
    }

    private fun addMessage(message: SQL_Message, onEnd: (Long) -> Unit){
            ctx.database.use {
                val exit = insert(
                    "Message",
                    "text" to message.text,
                    "time" to message.time
                )
                onEnd(exit)
                insert(
                    Helper.VIRTUAL_MESSAGE_TABLE,
                    "text" to message.text,
                    "_id" to exit
                )
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
    fun getAllMessageId(lambda:(List<Long>)->Unit){
        try {
            ctx.database.use {
                select("Message","_id")
                    .exec {
                        lambda(
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
            e.printStackTrace()
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

    fun getTagById(_id: Long,onEnd: (Tag) -> Unit){
        try {
            ctx.database.use {
                select("Tag")
                    .whereArgs("_id = {id}", "id" to _id)
                    .exec {
                        onEnd(
                            parseSingle(classParser())
                        )
                    }
            }
        }
        catch (e:Exception){
            e.printStackTrace()
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

    fun getAttachmentById(_id: Long, onEnd:(Attachment)->Unit) {
        try {
            ctx.database.use {
                select("Attachment")
                    .whereArgs("_id = {id}", "id" to _id)
                    .exec {
                        onEnd(
                            parseSingle(classParser())
                        )
                    }
            }
        }
        catch (e:Exception){
            e.printStackTrace()
        }

    }

    private fun addTag(tag:Tag, onEnd:(Long)->Unit){
            ctx.database.use {
                val out = insert(
                    "Tag",
                    "text" to tag.text
                )
                if (out ==-1L){
                    select("Tag","_id")
                        .whereArgs("text = {_text}","_text" to tag.text)
                        .exec {
                            onEnd(
                                parseSingle(object :MapRowParser<Long>{
                                    override fun parseRow(columns: Map<String, Any?>): Long {
                                        return columns.getValue("_id").toString().toLong()
                                    }
                                })
                            )
                        }
                }
                else{
                    insert(
                        Helper.VIRTUAL_TAG_TABLE,
                        "text" to tag.text,
                        "_id" to out
                    )
                    onEnd(out)
                }

            }

    }
    private fun addAttachment(tag:Attachment, onEnd: (Long) -> Unit){
            ctx.database.use {
                onEnd(
                insert(
                    "Attachment",
                    "type" to tag.type,
                    "link" to tag.link,
                    "parentId" to tag.parentId
                ))
            }

    }

    private fun addLink(tag: Link){
            ctx.database.use {
                insert(
                    "Link",
                    "messageId" to tag.messageId,
                    "tagId" to tag.tagId
                )
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
    private fun createSQL_message(message: Message): SQL_Message {
        return SQL_Message(message._id, message.text, message.time.time)
    }

    fun sendMessage(message:SQL_Message, tags:List<Tag>, attachments:List<Attachment>) {
        addMessage(message) { it ->
            Log.d("WORK", "end")
            val message_id = it
            for (i in tags) {
                addTag(i) { exit ->
                    Log.d("WORK", "end")
                    addLink(Link(123, message_id, exit))

                }
                for (a in attachments) {

                    addAttachment(Attachment(123, a.type, a.link, message_id)) {}
                }
            }
        }
    }

        fun getMessagesByTagId(_id: Long, callback: Callback<List<Long>>){
        try{
            callback.onBegin()
            ctx.database.use {
                select(
                    "Link",
                    "messageId"
                )
                    .whereArgs(
                        "tagId = {tag}",
                        "tag" to _id
                    )
                    .exec {
                        callback.onEnd(
                            parseList(object :MapRowParser<Long>{
                                override fun parseRow(columns: Map<String, Any?>): Long {
                                    return columns.getValue("messageId").toString().toLong()
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

    fun getMessagesByTagId(_id: Long, onEnd:(List<Long>)->Unit){
        try{
            ctx.database.use {
                select(
                    "Link",
                    "messageId"
                )
                    .whereArgs(
                        "tagId = {tag}",
                        "tag" to _id
                    )
                    .exec {
                        onEnd(
                            parseList(object :MapRowParser<Long>{
                                override fun parseRow(columns: Map<String, Any?>): Long {
                                    return columns.getValue("messageId").toString().toLong()
                                }
                            })
                        )
                    }
            }
        }
        catch (e:Exception){
            e.printStackTrace()
        }
    }


    fun getMessageIdsByTagPart(tagPart:String, callback: Callback<Collection<Long>>){
        try{
            ctx.database.use {
                select(
                    Helper.VIRTUAL_TAG_TABLE,
                    "_id"
                )
                    .whereArgs(
                        "text MATCH {tag}",
                        "tag" to "$tagPart*"
                    )
                    .exec {
                        val ids = parseList(
                            object :MapRowParser<Long>{
                                override fun parseRow(columns: Map<String, Any?>): Long {
                                    return columns.getValue("_id").toString().toLong()
                                }

                            }
                        )
                        val messageSet = mutableSetOf<Long>()
                        for (i in ids){
                            getMessagesByTagId(i) {
                                messageSet.addAll(it)
                            }
                        }
                        callback.onEnd(messageSet)

                    }
            }
        }
        catch(e:Exception){
            e.printStackTrace()
            callback.onFailure()
        }
    }

    fun getMessageIdsByTagPart(tagPart:String,onEnd:(Collection<Long>)->Unit){
        try{
            ctx.database.use {
                select(
                    Helper.VIRTUAL_TAG_TABLE,
                    "_id"
                )
                    .whereArgs(
                        "text MATCH {tag}",
                        "tag" to "$tagPart*"
                    )
                    .exec {
                        val ids = parseList(
                            object :MapRowParser<Long>{
                                override fun parseRow(columns: Map<String, Any?>): Long {
                                    return columns.getValue("_id").toString().toLong()
                                }

                            }
                        )
                        val messageSet = mutableSetOf<Long>()
                        for (i in ids){
                            getMessagesByTagId(i) {
                                messageSet.addAll(it)
                            }
                        }
                        onEnd(messageSet)

                    }
            }
        }
        catch(e:Exception){
            e.printStackTrace()
        }
    }



    fun getMessageByMessagePatr(messagePart:String, callback: Callback<List<Long>>){
        try{
            ctx.database.use {
                select(
                    Helper.VIRTUAL_MESSAGE_TABLE,
                    "_id"
                )
                    .whereArgs(
                        "text MATCH {tag}",
                        "tag" to "*$messagePart*"
                    )
                    .exec {
                        val ids = parseList(
                            object :MapRowParser<Long>{
                                override fun parseRow(columns: Map<String, Any?>): Long {
                                    return columns.getValue("_id").toString().toLong()
                                }

                            }
                        )
                        callback.onEnd(ids)

                    }
            }
        }
        catch(e:Exception){
            e.printStackTrace()
            callback.onFailure()
        }
    }

    fun getMessageByMessagePatr(messagePart:String, onEnd:(List<Long>)->Unit){
        try{
            ctx.database.use {
                select(
                    Helper.VIRTUAL_MESSAGE_TABLE,
                    "_id"
                )
                    .whereArgs(
                        "text MATCH {tag}",
                        "tag" to "*$messagePart*"
                    )
                    .exec {
                        val ids = parseList(
                            object :MapRowParser<Long>{
                                override fun parseRow(columns: Map<String, Any?>): Long {
                                    return columns.getValue("_id").toString().toLong()
                                }

                            }
                        )
                        onEnd(ids)

                    }
            }
        }
        catch(e:Exception){
            e.printStackTrace()
        }
    }

}