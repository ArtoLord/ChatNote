package com.yshmgrt.chat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yshmgrt.chat.R
import com.yshmgrt.chat.data_base.Controller
import com.yshmgrt.chat.data_base.dataclasses.SQL_Message
import kotlinx.android.synthetic.main.message_search_row.view.*
import kotlinx.android.synthetic.main.message_search_row.view.message_text
import java.text.SimpleDateFormat
import java.util.*

class MessageRowAdapter(val messages:List<Long>,val onClick:(View)->Unit): RecyclerView.Adapter<MessageRowViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageRowViewHolder {
        return MessageRowViewHolder(LayoutInflater.from(parent.context!!).inflate(R.layout.message_search_row,parent,false))
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: MessageRowViewHolder, position: Int) {
        val controller = Controller(holder.itemView.context)
        val id = messages[position]
        controller.getMessageById(id){
                holder.itemView.tag = id
                holder.itemView.message_text.text = it.text.replace("\n", " ").replace(Regex("[ ]+"), " ")
                val c = GregorianCalendar()
                c.time = it.time
                val dateFormat = SimpleDateFormat("d MMMM yyyy, HH:mm")
                holder.itemView.message_time.text = dateFormat.format(c.time)
                holder.itemView.setOnClickListener(onClick)
        }
    }
}


class MessageRowViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

}