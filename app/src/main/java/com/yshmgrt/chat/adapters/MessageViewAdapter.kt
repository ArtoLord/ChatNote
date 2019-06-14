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

class MessageViewAdapter(val messageIds:List<Long>, val ctx:Context): RecyclerView.Adapter<MessageView.MessageViewHolder>() {
    val controller:Controller =  Controller(ctx)
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MessageView.MessageViewHolder {
        val view = MessageView(parent.context)
        return MessageView.MessageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return messageIds.size
    }

    override fun onBindViewHolder(viewHolder: MessageView.MessageViewHolder, position: Int) {
        val id = messageIds[position]
        controller.getMessageById(id,object:Callback<Message>{
            override fun onFailure() {
                Log.d("WORK","failure")
            }

            override fun onBegin() {
                Log.d("WORK","begin")
            }

            override fun onEnd(exit: Message) {
                viewHolder.messageView.setMessageText(exit.text)
                Log.d("WORK",exit.text)
                for (i in exit.tags){
                    controller.getTagById(i,object : Callback<Tag>{
                        override fun onFailure() {
                            Log.d("WORK","failure")
                        }

                        override fun onBegin() {
                            return
                        }

                        override fun onEnd(exit: Tag) {
                            viewHolder.messageView.addTags(arrayOf(exit.text))
                        }

                    })
                }
            }

        })
    }
}

