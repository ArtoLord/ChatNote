package com.yshmgrt.chat.message

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.yshmgrt.chat.R
import com.yshmgrt.chat.data_base.Controller
import com.yshmgrt.chat.data_base.dataclasses.Message
import com.yshmgrt.chat.data_base.dataclasses.Tag
import kotlinx.android.synthetic.main.message_view.view.*
import kotlinx.android.synthetic.main.tag_view.view.*
import org.jetbrains.anko.childrenRecursiveSequence

class MessageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle){
    init {
        LayoutInflater.from(context)
            .inflate(R.layout.message_view, this, true)
    }


    private val tagList = mutableListOf<Tag>()
    fun setThisMessage(message:Long, controller: Controller, tagOnClick:(View)->Unit){

        controller.getMessageById(message){
            teg_field.removeAllViews()
            message_text.text = it.text
            Log.d("WORK",it.toString())
            for (i in it.tags)
                controller.getTagById(i){exit->
                    val tv = TagView(context)
                    tv.tag = exit._id
                    tv.tag_text.text = exit.text
                    tv.setOnClickListener(
                        tagOnClick
                    )
                    teg_field.addView(tv)
                }
        }
    }
    fun addAttachments(attachment: Array<View>){
        for(i in attachment) attachments.addView(i)
    }
    class MessageViewHolder(val messageView: MessageView): RecyclerView.ViewHolder(messageView)
}