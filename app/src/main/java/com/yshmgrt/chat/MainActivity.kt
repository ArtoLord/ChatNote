package com.yshmgrt.chat

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.yshmgrt.chat.data_base.Callback
import com.yshmgrt.chat.data_base.Controller
import com.yshmgrt.chat.data_base.Helper.Helper
import com.yshmgrt.chat.data_base.dataclasses.Message
import com.yshmgrt.chat.data_base.dataclasses.SQL_Message

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val c = Controller(applicationContext)
        c.getAllMessageId(object:Callback<List<Long>>{
            override fun onFailure() {
                Log.d("DATA WORK", "failure")
            }

            override fun onBegin() {
                Log.d("DATA WORK", "begin")
            }

            override fun onEnd(exit: List<Long>) {
                for (i in exit) Log.d("DATA WORK", "exsist $i")
            }

        })
    }
}
