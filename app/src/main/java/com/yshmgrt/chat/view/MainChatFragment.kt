package com.yshmgrt.chat.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.yshmgrt.chat.R
import com.yshmgrt.chat.adapters.MessageViewAdapter
import com.yshmgrt.chat.data_base.Callback
import com.yshmgrt.chat.data_base.Controller
import com.yshmgrt.chat.data_base.dataclasses.Attachment
import com.yshmgrt.chat.data_base.dataclasses.Message
import com.yshmgrt.chat.data_base.dataclasses.SQL_Message
import com.yshmgrt.chat.data_base.dataclasses.Tag
import com.yshmgrt.chat.message.logic.Logic
import kotlinx.android.synthetic.main.main_chat_fragment.*
import kotlinx.android.synthetic.main.main_chat_fragment.view.*
import java.util.*

class MainChatFragment : Fragment() {

    var adapter: MessageViewAdapter? = null
    private lateinit var linearLayoutManager: LinearLayoutManager
    val messageList = mutableListOf<Long>()

    fun updateMessageList(controller: Controller,tag:String = "", onUpdate:(List<Long>)->Unit){
            controller.getAllMessageId{ exit->
                messageList.clear()
                messageList.addAll(exit)
                onUpdate(exit)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.main_chat_fragment, container, false)
        adapter = MessageViewAdapter(messageList)
        linearLayoutManager = LinearLayoutManager(context!!.applicationContext)
        view.message_list_1.layoutManager = LinearLayoutManager(context!!.applicationContext)
        view.message_list_1.adapter = adapter

        val controller = Controller(context!!)
        view.send_button.setOnClickListener {
            if (view.message_edit_text.text.isNotEmpty()){
                val log = Logic(view.message_edit_text.text.toString()).getTags()
                val tags = List(log.size){Tag(123,log[it])}
                val attachments = listOf<Attachment>()
                val date = Calendar.getInstance().getTime()
                controller.sendMessage(SQL_Message(123,view.message_edit_text.text.toString(),date.time),tags,attachments){
                    Log.d("MESSAGE_WORK","sended")
                    updateMessageList(controller){
                        adapter!!.notifyDataSetChanged()
                        view.message_edit_text.text.clear()
                        view.message_list_1.smoothScrollToPosition(adapter!!.itemCount - 1)
                    }
                }

            }
        }
        updateMessageList(controller){
            adapter!!.notifyDataSetChanged()
            view.message_list_1.smoothScrollToPosition(adapter!!.itemCount - 1)
        }
        return view
    }

    override fun onStart() {
        super.onStart()
    }
}