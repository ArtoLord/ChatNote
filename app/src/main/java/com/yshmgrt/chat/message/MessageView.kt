package com.yshmgrt.chat.message

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.yshmgrt.chat.MainActivity
import com.yshmgrt.chat.R
import com.yshmgrt.chat.data_base.Controller
import com.yshmgrt.chat.data_base.dataclasses.Link
import com.yshmgrt.chat.data_base.dataclasses.Message
import com.yshmgrt.chat.data_base.dataclasses.SQL_Message
import com.yshmgrt.chat.data_base.dataclasses.Tag
import com.yshmgrt.chat.data_base.dataclasses.Tag.Companion.SYSTEM_TYPE
import com.yshmgrt.chat.message.attachments.IAttachment
import com.yshmgrt.chat.view.MessageDialog
import kotlinx.android.synthetic.main.message_view.view.*
import kotlinx.android.synthetic.main.tag_view.view.*
import org.jetbrains.anko.bundleOf
import ru.noties.markwon.Markwon
import ru.noties.markwon.core.CorePlugin
import ru.noties.markwon.ext.latex.JLatexMathPlugin
import ru.noties.markwon.image.ImagesPlugin
import java.text.SimpleDateFormat
import java.util.*


class MessageView constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle){
    init {
        LayoutInflater.from(context)
            .inflate(R.layout.message_view, this, true)
        this.message_text.setOnClickListener { this.callOnClick() }
        this.message_text.setOnLongClickListener { this.performLongClick() }
    }


    private val tagList = mutableListOf<Tag>()
    fun setThisMessage(message:Long, controller: Controller, tagOnClick:(View)->Unit){

        controller.getMessageById(message){
            tagList.clear()
            initDialog(it, controller, tagOnClick)
            Log.d("DEBUG",it.toString())
            if (it.type == SQL_Message.SYSTEM_TYPE){
                (notification_card.layoutParams as ConstraintLayout.LayoutParams).horizontalBias = 0f
            }
            else{
                (notification_card.layoutParams as ConstraintLayout.LayoutParams).horizontalBias = 1f
            }

            message_text.visibility = if (it.text == "")
                View.GONE
            else View.VISIBLE

            teg_field.removeAllViews()
            val  markwon = Markwon.builder(context)
                .usePlugin(CorePlugin.create())
                .usePlugin(JLatexMathPlugin.create(36F))
                .usePlugin(ImagesPlugin.create(context))
                .build()

            markwon.setMarkdown(message_text,it.text)
            val c = GregorianCalendar()
            c.time = it.time
            val dateFormat = SimpleDateFormat("HH:mm")
            message_time.text = dateFormat.format(c.time)
            tag = it._id
            Log.d("WORK",it.toString())
            if (it.tags.isEmpty())
                tags_linear.visibility = View.GONE
            else
                tags_linear.visibility = View.VISIBLE
            for (i in it.tags)
                controller.getTagById(i) { exit ->
                    if (exit.type == Tag.USER_TYPE) {
                        val tv = TagView(context)
                        tv.tag = exit._id
                        tv.tag_text.text = exit.text
                        tv.setOnClickListener(
                            tagOnClick
                        )
                        teg_field.addView(tv)
                    }
                    tagList.add(exit)
                }
            attachments.removeAllViews()
            for (i in it.attachment){
                controller.getAttachmentById(i){exit->
                    val attach = IAttachment.create(exit)
                    attachments.addView(attach!!.getMessageView(context))
                }
            }
        }
    }

    private fun initDialog(message : Message, controller: Controller, tagOnClick:(View)->Unit) {
        val dialogActions = listOf({
            val bundle = bundleOf("messageId" to message._id)
            Navigation.findNavController(this).navigate(R.id.action_mainChatFragment_to_messageFragment,bundle)
            (context as MainActivity).moveToMessageDetails()
        }, {
            (context as MainActivity).onMessageUpdate(message._id)
        }, {
            AlertDialog.Builder(context)
                .setTitle(R.string.want_to_delete_1)
                .setMessage(R.string.want_to_delete_2)
                .setPositiveButton(R.string.yes){_,_->
                    Controller(context).deleteMessageById(message._id) {
                        (context as MainActivity).onMessageDelete()
                    }
                }
                .setNegativeButton(R.string.no){_,_->}
                .show()

        }, {
            val bookmarkTag = Tag(123, "#bookmark", SYSTEM_TYPE)
            controller.addTag(bookmarkTag) {tagId ->
                if (tagList.count { it.text == "#bookmark" } == 0)
                    controller.addLink(Link(123, message._id, tagId)) {
                        Log.d("TESTI", "OK")
                        setThisMessage(message._id, controller, tagOnClick)
                    }
                else
                    controller.deleteLink(Link(123, message._id, tagId)) {
                        Log.d("TESTI", "OKI")
                        setThisMessage(message._id, controller, tagOnClick)
                    }
            }
        })
        this.setOnLongClickListener {
            Log.d("TESTI", message.tags.joinToString { it.toString() })
            MessageDialog(dialogActions, tagList.count { it.text == "#bookmark" } == 0)
                .show((context as AppCompatActivity).supportFragmentManager, "MessageDialogFragment")
            true
        }
    }


    class MessageViewHolder(val messageView: MessageView): RecyclerView.ViewHolder(messageView)
}