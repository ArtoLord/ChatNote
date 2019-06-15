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
import com.yshmgrt.chat.message.OpenMessageView

class MessageViewAdapter(val messageIds:List<Long>): RecyclerView.Adapter<MessageView.MessageViewHolder>() {
    lateinit var controller:Controller
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): MessageView.MessageViewHolder {
        controller = Controller(parent.context)
        var view = MessageView(parent.context)
        return MessageView.MessageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return messageIds.size
    }

    override fun onBindViewHolder(viewHolder: MessageView.MessageViewHolder, position: Int) {
        val id = messageIds[position]
        controller.getMessageById(id){
                viewHolder.messageView.setMessageText(it.text)
                Log.d("WORK",it.toString())
                for (i in it.tags)
                    controller.getTagById(i){exit->viewHolder.messageView.addTags(listOf(exit.text)) }
            }
    }
}

