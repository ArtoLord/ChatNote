package com.yshmgrt.chat

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import com.yshmgrt.chat.adapters.MessageViewAdapter
import com.yshmgrt.chat.data_base.Callback
import com.yshmgrt.chat.data_base.Controller
import com.yshmgrt.chat.data_base.dataclasses.Link
import com.yshmgrt.chat.data_base.dataclasses.SQL_Message
import com.yshmgrt.chat.data_base.dataclasses.Tag
import com.yshmgrt.chat.message.attachments.Image
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var adapter:MessageViewAdapter? = null
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        message_list.setAdapter(adapter)
        linearLayoutManager = LinearLayoutManager(this)
        message_list.layoutManager = linearLayoutManager
        setSupportActionBar(toolbar)
        val controller = Controller(applicationContext)
        controller.getAllMessageId(object:Callback<List<Long>>{
            override fun onFailure() {
                return
            }

            override fun onBegin() {

                return
            }

            override fun onEnd(exit: List<Long>) {
                adapter = MessageViewAdapter(exit,applicationContext)
                message_list.adapter = adapter
                adapter!!.notifyDataSetChanged()
            }
        })



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
