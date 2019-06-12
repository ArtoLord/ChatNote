package com.yshmgrt.chat.message

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.yshmgrt.chat.R
import kotlinx.android.synthetic.main.message_view.view.*
import kotlinx.android.synthetic.main.tag_view.view.*

class MessageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle){
    init {
        LayoutInflater.from(context)
            .inflate(R.layout.message_view, this, true)
    }


    fun setMessageText(text:String){
        message_text.text = text
    }
    fun addTags(tags:Array<String>){
        for (i in tags){
            val tag = TagView(this.context)
            tag.setText(i)
            teg_field.addView(tag)
        }
    }
    fun addAttachment(attachment: Attachment){
        linear.addView(attachment)
    }
}