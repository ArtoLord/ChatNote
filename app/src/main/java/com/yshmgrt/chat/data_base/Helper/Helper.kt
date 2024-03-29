package com.yshmgrt.chat.data_base.Helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.yshmgrt.chat.data_base.Callback
import com.yshmgrt.chat.data_base.IController
import com.yshmgrt.chat.data_base.dataclasses.Attachment
import com.yshmgrt.chat.data_base.dataclasses.Message
import com.yshmgrt.chat.data_base.dataclasses.Tag
import org.jetbrains.anko.db.*
import org.jetbrains.anko.doAsync

class Helper private constructor(ctx: Context):ManagedSQLiteOpenHelper(ctx, "MainDatabase", null, 1) {



    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.dropTable(VIRTUAL_MESSAGE_TABLE, true)
        db.dropTable(VIRTUAL_TAG_TABLE, true)
        db.dropTable("Message", true)
        db.dropTable("Attachment", true)
        db.dropTable("Tag", true)
        db.dropTable("Link", true)

    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE VIRTUAL TABLE IF NOT EXISTS $VIRTUAL_MESSAGE_TABLE USING FTS4(text, _id UNINDEXED);")

        db.execSQL("CREATE VIRTUAL TABLE IF NOT EXISTS $VIRTUAL_TAG_TABLE USING FTS4(text, _id UNINDEXED);")


        db.createTable("Message", true,
            "_id" to INTEGER+ PRIMARY_KEY+ UNIQUE,
            "text" to TEXT,
            "time" to INTEGER,
            "type" to INTEGER
            )
        db.createTable("Attachment", true,
            "_id" to INTEGER+ PRIMARY_KEY+ UNIQUE,
            "type" to TEXT,
            "link" to TEXT,
            "parentId" to INTEGER
        )
        db.createTable("Tag", true,
            "_id" to INTEGER+ PRIMARY_KEY+ UNIQUE,
            "text" to TEXT+ UNIQUE,
            "type" to INTEGER
        )
        db.createTable("Link", true,
            "_id" to INTEGER+ PRIMARY_KEY+ UNIQUE,
            "messageId" to INTEGER,
            "tagId" to INTEGER
        )
    }

    init {
        instance = this
    }
    companion object {
        private var instance: Helper? = null
        val VIRTUAL_MESSAGE_TABLE = "virtual_message"
        val VIRTUAL_TAG_TABLE = "virtual_message"

        @Synchronized
        fun getInstance(ctx: Context) = instance ?: Helper(ctx.applicationContext)
    }






}
