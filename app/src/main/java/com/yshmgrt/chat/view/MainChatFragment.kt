package com.yshmgrt.chat.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yshmgrt.chat.R
import com.yshmgrt.chat.adapters.MessageViewAdapter
import com.yshmgrt.chat.data_base.Callback
import com.yshmgrt.chat.data_base.Controller
import com.yshmgrt.chat.data_base.dataclasses.Attachment
import com.yshmgrt.chat.data_base.dataclasses.Message
import com.yshmgrt.chat.data_base.dataclasses.SQL_Message
import com.yshmgrt.chat.data_base.dataclasses.Tag
import com.yshmgrt.chat.message.logic.Logic
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_chat_fragment.*
import kotlinx.android.synthetic.main.main_chat_fragment.view.*
import kotlinx.android.synthetic.main.tag_view.view.*
import org.jetbrains.anko.bundleOf
import java.util.*

class MainChatFragment : Fragment() {

    var adapter: MessageViewAdapter? = null
    private lateinit var linearLayoutManager: LinearLayoutManager
    val messageList = mutableListOf<Long>()
    fun updateMessageList(controller: Controller,tag:Long = -1, onUpdate:(List<Long>)->Unit){
        if (tag == -1L){
                controller.getAllMessageId{ exit->
                    messageList.clear()
                    messageList.addAll(exit)
                    onUpdate(exit)
            }
        }
        else{
            controller.getMessagesByTagId(tag){exit->
                messageList.clear()
                messageList.addAll(exit)
                onUpdate(exit)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.main_chat_fragment, container, false)
        val controller = Controller(context!!)
        adapter = MessageViewAdapter(messageList,{
            val _id = it.tag.toString().toLong()
            updateMessageList(controller,_id){
                adapter!!.notifyDataSetChanged()
                view.message_edit_text.text.clear()
                view.message_list_1.smoothScrollToPosition(adapter!!.itemCount - 1)
            }
        }){
            val _id = it.tag.toString().toLong()
            val bundle = bundleOf("messageId" to _id)

            Navigation.findNavController(view).navigate(R.id.action_mainChatFragment_to_messageFragment,bundle)
        }
        linearLayoutManager = LinearLayoutManager(context!!.applicationContext)
        linearLayoutManager.stackFromEnd = true
        view.message_list_1.layoutManager = linearLayoutManager
        view.message_list_1.adapter = adapter


        view.send_button.setOnClickListener {
            if (view.message_edit_text.text.isNotEmpty()){
                val log = Logic(view.message_edit_text.text.toString()).getTags()
                val tags = List(log.size){Tag(123,log[it])}
                val attachments = listOf<Attachment>()
                controller.sendMessage(SQL_Message(123,view.message_edit_text.text.toString(),Date().time),tags,attachments){
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