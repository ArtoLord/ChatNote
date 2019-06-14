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
import kotlinx.android.synthetic.main.main_chat_fragment.*
import kotlinx.android.synthetic.main.main_chat_fragment.view.*

class MainChatFragment : Fragment() {

    var adapter: MessageViewAdapter? = null
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_chat_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        linearLayoutManager = LinearLayoutManager(context!!)
        val list = listOf(1L, 2L, 3L)
        message_list.layoutManager = LinearLayoutManager(activity)
        message_list.adapter = MessageViewAdapter(list, activity!!)
    }

    override fun onStart() {
        super.onStart()


        val controller = Controller(context!!)
        controller.getAllMessageId(object: Callback<List<Long>> {
            override fun onFailure() {
                Log.d("WORK","failure")
            }

            override fun onBegin() {

                Log.d("WORK","begin")
            }

            override fun onEnd(exit: List<Long>) {
                adapter = MessageViewAdapter(exit, context!!)
                Log.d("WORK",exit.size.toString())
                message_list.adapter = adapter
                adapter!!.notifyDataSetChanged()
            }
        })
        adapter?.notifyDataSetChanged()
    }
}