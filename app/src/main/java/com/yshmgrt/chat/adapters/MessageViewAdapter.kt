package com.yshmgrt.chat.adapters

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yshmgrt.chat.data_base.Controller
import com.yshmgrt.chat.message.MessageView

class MessageViewAdapter(val messageIds:List<Long>,
                         val tagOnClick:(View)->Unit,
                         val messageOnClick:(View)->Unit): RecyclerView.Adapter<MessageView.MessageViewHolder>() {
    lateinit var controller:Controller
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): MessageView.MessageViewHolder {
        controller = Controller(parent.context)
        val view = MessageView(parent.context)
        view.layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return MessageView.MessageViewHolder(view)
    }
    override fun getItemCount(): Int {
        return messageIds.size
    }

    override fun onBindViewHolder(viewHolder: MessageView.MessageViewHolder, position: Int) {
        val id = messageIds[position]
        Log.d("WORK",id.toString())
        viewHolder.messageView.setThisMessage(id,controller,tagOnClick)
        viewHolder.messageView.setOnClickListener {
            messageOnClick(it)
        }

    }
}
