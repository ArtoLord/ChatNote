package com.yshmgrt.chat

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.yshmgrt.chat.message.Attachment
import com.yshmgrt.chat.message.MessageView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        message.setMessageText("It works aaaaaaaaaaaaaaaaaa")
        message.addTags(arrayOf("#Hello","#239_forever","#jb_project"))
    }


}
