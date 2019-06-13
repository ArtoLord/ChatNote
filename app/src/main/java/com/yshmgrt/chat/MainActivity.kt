package com.yshmgrt.chat

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.yshmgrt.chat.message.AttachmentView
import com.yshmgrt.chat.message.attachments.Image
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        message.setMessageText("It works)")
        message.addTags(arrayOf("#Hello","#239_forever","#jb_project", "#Some_tag","#More_tags"))
        val a = Image(message.context)
        a.setImage(getDrawable(R.drawable.android))
        message.addAttachments(arrayOf(a))
    }


}
