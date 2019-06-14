package com.yshmgrt.chat.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.yshmgrt.chat.R
import com.yshmgrt.chat.adapters.MessageViewAdapter
import com.yshmgrt.chat.data_base.Callback
import com.yshmgrt.chat.data_base.Controller
import com.yshmgrt.chat.data_base.dataclasses.SQL_Message
import com.yshmgrt.chat.data_base.dataclasses.Tag
import kotlinx.android.synthetic.main.main_chat_fragment.*
import kotlinx.android.synthetic.main.main_chat_fragment.view.*

class MainChatFragment : Fragment() {

    var adapter: MessageViewAdapter? = null
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.main_chat_fragment, container, false)
        linearLayoutManager = LinearLayoutManager(context!!.applicationContext)
        view.message_list_1.layoutManager = LinearLayoutManager(context!!.applicationContext)
        view.message_list_1.adapter = MessageViewAdapter(emptyList())
        Log.d("Work", view.message_list_1.adapter.toString())
        val controller = Controller(context!!)
        controller.getAllMessageId(object: Callback<List<Long>> {
            override fun onFailure() {
                Log.d("WORK","message failure")
            }

            override fun onBegin() {

                Log.d("WORK","begin")
            }

            override fun onEnd(exit: List<Long>) {
                adapter = MessageViewAdapter(exit)
                Log.d("WORK",exit.size.toString())
                view.message_list_1.adapter = adapter
                Log.d("Work", view.message_list_1.adapter.toString() + " 2")
                adapter!!.notifyDataSetChanged()
            }
        })
        Log.d("Work", view.message_list_1.adapter.toString() + " 1")
        return view
    }

    override fun onStart() {
        Log.d("Work", message_list_1.adapter.toString() + " 3")
        super.onStart()
    }
}