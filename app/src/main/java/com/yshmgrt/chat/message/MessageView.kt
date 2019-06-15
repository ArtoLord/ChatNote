package com.yshmgrt.chat.message

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.yshmgrt.chat.R
import kotlinx.android.synthetic.main.message_view.view.*
import org.jetbrains.anko.childrenRecursiveSequence

open class MessageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle){
    init {
        LayoutInflater.from(context)
            .inflate(R.layout.message_view, this, true)
    }


    private val tagList = mutableListOf<String>()
    fun setMessageText(text:String){
        message_text.text = text
    }
    fun addTags(tags:Collection<String>){
        for (i in tags){
            if (i !in tagList) {
                tagList.add(i)
                val tag = TagView(this.context)
                tag.setText(i)
                teg_field.addView(tag)
            }
        }
    }
    fun addAttachments(attachment: Array<View>){
        for(i in attachment) attachments.addView(i)
    }
    class MessageViewHolder(val messageView: MessageView): RecyclerView.ViewHolder(messageView)
}


class OpenMessageView(context: Context): MessageView(context) {
    init {
        LayoutInflater.from(context)
            .inflate(R.layout.message_view_opened, this, true)
    }

}