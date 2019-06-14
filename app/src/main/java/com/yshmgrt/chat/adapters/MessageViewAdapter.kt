package com.yshmgrt.chat.adapters

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yshmgrt.chat.data_base.Callback
import com.yshmgrt.chat.data_base.Controller
import com.yshmgrt.chat.data_base.dataclasses.Message
import com.yshmgrt.chat.data_base.dataclasses.Tag
import com.yshmgrt.chat.message.MessageView

class MessageViewAdapter(val messageIds:List<Long>): RecyclerView.Adapter<MessageView.MessageViewHolder>() {
    lateinit var controller:Controller
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MessageView.MessageViewHolder {
        controller = Controller(parent.context)
        val view = MessageView(parent.context)

        return MessageView.MessageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return messageIds.size
    }

    override fun onBindViewHolder(viewHolder: MessageView.MessageViewHolder, position: Int) {
        val id = messageIds[position]
        controller.getMessageById(id){
                viewHolder.messageView.setMessageText(it.text)
                Log.d("WORK",it.text)
                for (i in it.tags)
                    controller.getTagById(i){exit->viewHolder.messageView.addTags(listOf(exit.text)) }
            }
    }
}

