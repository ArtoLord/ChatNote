package com.yshmgrt.chat.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yshmgrt.chat.data_base.Controller
import com.yshmgrt.chat.message.MessageView
import com.yshmgrt.chat.message.TagView
import kotlinx.android.synthetic.main.message_view.view.*
import kotlinx.android.synthetic.main.tag_view.view.*

class TagViewAdapter(val tagIds:List<Long>,
                         val tagOnClick:(View)->Unit): RecyclerView.Adapter<TagView.TagViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagView.TagViewHolder {
        controller = Controller(parent.context)
        return TagView.TagViewHolder(TagView(parent.context))
    }

    override fun getItemCount(): Int {
        return tagIds.size
    }

    override fun onBindViewHolder(holder: TagView.TagViewHolder, position: Int) {
        val view = holder.tagView
        controller.getTagById(tagIds[position]){
            view.tag_text.text = it.text
            view.tag = it._id
            view.setOnClickListener { tagOnClick }
        }
    }

    lateinit var controller: Controller

}