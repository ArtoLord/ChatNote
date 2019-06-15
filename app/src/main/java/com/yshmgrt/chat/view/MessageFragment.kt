package com.yshmgrt.chat.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.yshmgrt.chat.R
import com.yshmgrt.chat.data_base.Controller
import com.yshmgrt.chat.message.TagView
import kotlinx.android.synthetic.main.message_fragment.*
import kotlinx.android.synthetic.main.message_view.view.*
import kotlinx.android.synthetic.main.tag_view.view.*
import java.util.*

class MessageFragment:Fragment() {
    var _id = -1L
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.message_fragment, container, false)
        _id = arguments!!.getLong("messageId")
        val controller = Controller(context!!)
        controller.getMessageById(_id){
                view.teg_field.removeAllViews()
                view.message_text.text = it.text
                Log.d("WORK",it.toString())
                for (i in it.tags)
                    controller.getTagById(i){exit->
                        val tv = TagView(context!!)
                        tv.tag = exit._id
                        tv.tag_text.text = exit.text
                        view.teg_field.addView(tv)
                    }

        }
        return view
    }
}