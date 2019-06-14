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

class Helper private constructor(ctx: Context):ManagedSQLiteOpenHelper(ctx, "MainDatabase", null, 2) {

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.dropTable("Message", true)
        db.dropTable("Attachment", true)
        db.dropTable("Tag", true)
        db.dropTable("Link", true)

    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable("Message", true,
            "_id" to INTEGER+ PRIMARY_KEY+ UNIQUE,
            "text" to TEXT,
            "time" to INTEGER
            )
        db.createTable("Attachment", true,
            "_id" to INTEGER+ PRIMARY_KEY+ UNIQUE,
            "type" to TEXT,
            "link" to TEXT,
            "parentId" to INTEGER
        )
        db.createTable("Tag", true,
            "_id" to INTEGER+ PRIMARY_KEY+ UNIQUE,
            "text" to TEXT
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

        @Synchronized
        fun getInstance(ctx: Context) = instance ?: Helper(ctx.applicationContext)
    }






}
